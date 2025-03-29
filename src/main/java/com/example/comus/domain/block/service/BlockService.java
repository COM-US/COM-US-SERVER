package com.example.comus.domain.block.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.block.dto.request.BlockPlaceRequestDto;
import com.example.comus.domain.block.dto.response.BlockCountResponseDto;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.global.error.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.comus.global.error.ErrorCode.BLOCK_CATEGORY_NOT_FOUND;

@AllArgsConstructor
@Transactional
@Service
public class BlockService {

    private final AnswerRepository answerRepository;
    private final BlockRepository blockRepository;
    private final static int BLOCK_SIZE = 4;

    // 블럭 개수 조회
    public BlockCountResponseDto getBlockCount(Long userId) {
        Map<QuestionCategory, Integer> countMap = Arrays.stream(QuestionCategory.values()).collect(Collectors.toMap(category -> category, category -> {
            Long count = answerRepository.countByUserIdAndQuestionCategoryAndIsUsedFalse(userId, category);
            return count != null ? count.intValue() : 0;
        }));

        return BlockCountResponseDto.of(countMap.get(QuestionCategory.DAILY), countMap.get(QuestionCategory.SCHOOL), countMap.get(QuestionCategory.HOBBY), countMap.get(QuestionCategory.FAMILY), countMap.get(QuestionCategory.FRIEND));
    }


    // 블록 배치
    @Transactional
    public void createBlock(Long userId, BlockPlaceRequestDto blockPlaceRequestDto) {

        Answer answer = getUnusedAnswer(userId, blockPlaceRequestDto.questionCategory());

        int[][] blockPlace = blockPlaceRequestDto.blockPlace();

        for (int row = 0; row < blockPlace.length; row++) {
            for (int col = 0; col < blockPlace[row].length; col++) {
                if (blockPlace[row][col] == 1) {
                    Block block = Block.builder()
                            .level(1)
                            .blockRow(row)
                            .blockColumn(col)
                            .answer(answer)
                            .build();

                    blockRepository.save(block);
                }
            }
        }
        answer.setUsed();
    }

    private Answer getUnusedAnswer(Long userId, QuestionCategory questionCategory) {
        List<Answer> answers = answerRepository.findByUserIdAndQuestionCategoryAndIsUsedFalseOrderByCreatedAtAsc(userId, questionCategory);
        if (answers.isEmpty()) {
            throw new BusinessException(BLOCK_CATEGORY_NOT_FOUND);
        }
        return answers.get(0);
    }


}

