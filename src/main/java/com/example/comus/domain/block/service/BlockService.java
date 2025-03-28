package com.example.comus.domain.block.service;

import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.block.dto.response.BlockCountResponseDto;
import com.example.comus.domain.question.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class BlockService {

    private final AnswerRepository answerRepository;

    // 블럭 개수 조회
    public BlockCountResponseDto getBlockCount(Long userId) {
        Map<QuestionCategory, Integer> countMap = Arrays.stream(QuestionCategory.values()).collect(Collectors.toMap(category -> category, category -> {
            Long count = answerRepository.countByUserIdAndQuestionCategoryAndIsUsedFalse(userId, category);
            return count != null ? count.intValue() : 0;
        }));

        return BlockCountResponseDto.of(countMap.get(QuestionCategory.DAILY), countMap.get(QuestionCategory.SCHOOL), countMap.get(QuestionCategory.HOBBY), countMap.get(QuestionCategory.FAMILY), countMap.get(QuestionCategory.FRIEND));
    }


}
