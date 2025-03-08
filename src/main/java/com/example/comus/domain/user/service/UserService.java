package com.example.comus.domain.user.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.domain.user.dto.request.LoginRequestDto;
import com.example.comus.domain.user.dto.response.*;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.example.comus.global.config.auth.jwt.JwtProvider;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    public String issueNewAccessToken(Long memberId) {
        return jwtProvider.getIssueToken(memberId, true);
    }

    public String issueNewRefreshToken(Long memberId) {
        return jwtProvider.getIssueToken(memberId, false);
    }

    public UserTokenResponseDto getToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        String refreshToken = issueNewRefreshToken(userId);
        return UserTokenResponseDto.of(accessToken, refreshToken);
    }

    // 로그인
    @Transactional
    public UserTokenResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findBySocialIdAndSocialType(loginRequest.socialId(), loginRequest.socialType())
                .orElseGet(() -> userRepository.save(loginRequest.toEntity()));
        return getToken(user.getId());
    }

    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .todayChatTime(user.getTodayChatTime())
                .totalChatTime(user.getTotalChatTime())
                .totalChatCount(user.getTotalChatCount())
                .build();
    }

    public MainPageUserresponseDto getMainPageUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return MainPageUserresponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .todayChatTime(user.getTodayChatTime())
                .build(
                );
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

        int randomCount = calculateUniqueQuestionCountByCategory(answers, QuestionCategory.RANDOM);
        int randomTotalCount = getTotalCountByCategory(QuestionCategory.RANDOM);
        int randomPercent = calculatePercentage(randomCount, randomTotalCount);

        return CategoryResponseDto.builder()
                .DailyCount(dailyCount)
                .DailyTotalCount(dailyTotalCount)
                .DailyPercent(dailyPercent)
                .SchoolCount(schoolCount)
                .SchoolTotalCount(schoolTotalCount)
                .SchoolPercent(schoolPercent)
                .FriendCount(friendCount)
                .FriendTotalCount(friendTotalCount)
                .FriendPercent(friendPercent)
                .FamilyCount(familyCount)
                .FamilyTotalCount(familyTotalCount)
                .FamilyPercent(familyPercent)
                .HobbyCount(hobbyCount)
                .HobbyTotalCount(hobbyTotalCount)
                .HobbyPercent(hobbyPercent)
                .RandomCount(randomCount)
                .RandomTotalCount(randomTotalCount)
                .RandomPercent(randomPercent)
                .build();
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
