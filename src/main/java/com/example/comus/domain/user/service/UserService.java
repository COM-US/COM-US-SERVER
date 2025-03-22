package com.example.comus.domain.user.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.domain.user.dto.request.LoginRequestDto;
import com.example.comus.domain.user.dto.request.UserTokenRequestDto;
import com.example.comus.domain.user.dto.response.*;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.example.comus.global.config.auth.jwt.JwtProvider;
import com.example.comus.global.error.exception.EntityNotFoundException;
import com.example.comus.global.error.exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.comus.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final JwtProvider jwtProvider;
    private final UserRespository userRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public String issueNewAccessToken(Long memberId) {
        return jwtProvider.getIssueToken(memberId, true);
    }

    public String issueNewRefreshToken(Long memberId) {
        return jwtProvider.getIssueToken(memberId, false);
    }

    // 임시 토큰 발급
    public UserTokenResponseDto getTempToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        String refreshToken = issueNewRefreshToken(userId);
        return UserTokenResponseDto.of(accessToken, refreshToken);
    }

    // 로그인
    @Transactional
    public UserTokenResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findBySocialIdAndSocialType(loginRequest.socialId(), loginRequest.socialType())
                .orElseGet(() -> userRepository.save(loginRequest.toEntity()));

        Long userId = user.getId();
        String accessToken = jwtProvider.getIssueToken(userId, true);

        // refresh token이 이미 존재하면 기존 토큰 사용하고, 없으면 새로 발급
        String redisKey = "RT:" + userId;
        String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);
        if (storedRefreshToken == null) {
            storedRefreshToken = issueNewRefreshToken(userId);
            redisTemplate.opsForValue().set(redisKey, storedRefreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        }

        return UserTokenResponseDto.of(accessToken, storedRefreshToken);
    }

    // 로그아웃
    public void logout(Long userId) {
        String redisKey = "RT:" + userId;
        redisTemplate.delete(redisKey);
    }

    // 토큰 재발급
    public UserTokenResponseDto reissue(UserTokenRequestDto userTokenRequest) throws JsonProcessingException {
        Long userId = Long.valueOf(jwtProvider.decodeJwtPayloadSubject(userTokenRequest.accessToken()));

        String refreshToken = userTokenRequest.refreshToken();
        String redisKey = "RT:" + userId;

        // 리프레시 토큰 검증 (리프레시 토큰 만료시 재로그인 필요)
        jwtProvider.validateRefreshToken(refreshToken);

        String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

        // 요청된 리프레시 토큰과 저장된 redis 저장된 리프레시 토큰 비교 검증
        jwtProvider.equalsRefreshToken(refreshToken, storedRefreshToken);

        String newAccessToken = issueNewAccessToken(userId);
        String newRefreshToken = issueNewRefreshToken(userId);

        redisTemplate.opsForValue().set(redisKey, newRefreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        return UserTokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return UserInfoResponseDto.from(user);
    }

    public MainPageUserresponseDto getMainPageUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return MainPageUserresponseDto.from(user);
    }

    public List<BlockResponseDto> getBlockList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        List<Answer> answers = user.getAnswers();
        List<Block> blocks = new ArrayList<>();

        for (Answer answer : answers) {
            List<Block> answerBlocks = blockRepository.findByAnswer(answer);
            blocks.addAll(answerBlocks);
        }

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto getCategory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        List<Answer> answers = user.getAnswers();

        int dailyCount = calculateUniqueQuestionCountByCategory(answers, QuestionCategory.DAILY);
        int dailyTotalCount = getTotalCountByCategory(QuestionCategory.DAILY);
        int dailyPercent = calculatePercentage(dailyCount, dailyTotalCount);

        int schoolCount = calculateUniqueQuestionCountByCategory(answers, QuestionCategory.SCHOOL);
        int schoolTotalCount = getTotalCountByCategory(QuestionCategory.SCHOOL);
        int schoolPercent = calculatePercentage(schoolCount, schoolTotalCount);

        int friendCount = calculateUniqueQuestionCountByCategory(answers, QuestionCategory.FRIEND);
        int friendTotalCount = getTotalCountByCategory(QuestionCategory.FRIEND);
        int friendPercent = calculatePercentage(friendCount, friendTotalCount);

        int familyCount = calculateUniqueQuestionCountByCategory(answers, QuestionCategory.FAMILY);
        int familyTotalCount = getTotalCountByCategory(QuestionCategory.FAMILY);
        int familyPercent = calculatePercentage(familyCount, familyTotalCount);

        int hobbyCount = calculateUniqueQuestionCountByCategory(answers, QuestionCategory.HOBBY);
        int hobbyTotalCount = getTotalCountByCategory(QuestionCategory.HOBBY);
        int hobbyPercent = calculatePercentage(hobbyCount, hobbyTotalCount);


        return CategoryResponseDto.from(
                dailyCount, dailyTotalCount, dailyPercent,
                schoolCount, schoolTotalCount, schoolPercent,
                friendCount, friendTotalCount, friendPercent,
                familyCount, familyTotalCount, familyPercent,
                hobbyCount, hobbyTotalCount, hobbyPercent
        );
    }

    private int calculateUniqueQuestionCountByCategory(List<Answer> answers, QuestionCategory category) {
        return (int) answers.stream()
                .map(Answer::getQuestion)
                .filter(question -> question.getCategory() == category)
                .distinct()
                .count();
    }

    private int getTotalCountByCategory(QuestionCategory category) {
        return questionRepository.countByCategory(category);
    }

    private int calculatePercentage(int count, int totalCount) {
        return totalCount > 0 ? (count * 100) / totalCount : 0;
    }

}
