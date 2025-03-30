package com.example.comus.domain.user.service;

import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.block.dto.response.BlockResponseDto;
import com.example.comus.domain.block.service.BlockService;
import com.example.comus.domain.question.dto.response.RandomQuestionResponseDto;
import com.example.comus.domain.question.repository.QuestionLikeRepository;
import com.example.comus.domain.question.service.QuestionService;
import com.example.comus.domain.user.dto.request.LoginRequestDto;
import com.example.comus.domain.user.dto.request.UserTokenRequestDto;
import com.example.comus.domain.user.dto.response.MainPageResponseDto;
import com.example.comus.domain.user.dto.response.UserInfoResponseDto;
import com.example.comus.domain.user.dto.response.UserTokenResponseDto;
import com.example.comus.domain.answer.dto.response.WeeklyAnswerResponseDto;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.example.comus.global.config.auth.jwt.JwtProvider;
import com.example.comus.global.error.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.comus.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final JwtProvider jwtProvider;
    private final UserRespository userRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final AnswerRepository answerRepository;
    private final AnswerService answerService;
    private final BlockService blockService;
    private final QuestionService questionService;

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

    public MainPageResponseDto getMainPage(Long userId) {
        UserInfoResponseDto user = getUserInfo(userId);
        List<BlockResponseDto> blocks = blockService.getBlock(userId);
        RandomQuestionResponseDto randomQuestion = questionService.getRandomQuestion();
        return MainPageResponseDto.of(user,randomQuestion,blocks);
    }

    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        int answerCount = answerRepository.countByUserId(userId);
        int likeCount = questionLikeRepository.countByUserId(userId);

        List<WeeklyAnswerResponseDto> weeklyAnswers = answerService.getWeeklyAnswers(userId);

        return UserInfoResponseDto.of(user, answerCount, likeCount, weeklyAnswers);
    }

}
