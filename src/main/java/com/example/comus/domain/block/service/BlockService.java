package com.example.comus.domain.block.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.global.error.ErrorCode;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void save(long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANSWER_NOT_FOUND));

        Answer lastAnswer = answerRepository.findSecondLatestAnswer()
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANSWER_NOT_FOUND));

        Question question = questionRepository.findById(answer.getQuestion().getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        Block lastBlock = blockRepository.findByAnswerId(lastAnswer.getId())
                .orElse(null);

        int newLevel = 1;
        int newRow = 0;
        int newColumn = 0;

        if (lastBlock != null) {
            newLevel = lastBlock.getLevel();
            newRow = lastBlock.getBlockRow();
            newColumn = lastBlock.getBlockColumn();

            if (newColumn < 3) {
                newColumn++;
            } else if (newRow < 3) {
                newColumn = 0;
                newRow++;
            } else {
                newLevel++;
                newRow = 0;
                newColumn = 0;
            }
        }

        // 블록 수 계산
        int blockCount = getBlockCountByCategory(question.getCategory());

        for (int i = 0; i < blockCount; i++) {
            // 중복 블록을 방지하기 위해 위치 검증
            while (blockRepository.existsByLevelAndBlockRowAndBlockColumn(newLevel, newRow, newColumn)) {
                // 위치를 변경할 로직 추가
                newColumn++;
                if (newColumn >= 4) {
                    newColumn = 0;
                    newRow++;
                    if (newRow >= 4) {
                        newRow = 0;
                        newLevel++;
                    }
                }
            }


            Block block = Block.builder()
                    .level(newLevel)
                    .blockRow(newRow)
                    .blockColumn(newColumn)
                    .answer(answer)
                    .build();


            blockRepository.save(block);


            newColumn++;
            if (newColumn >= 4) {
                newColumn = 0;
                newRow++;
                if (newRow >= 4) {
                    newRow = 0;
                    newLevel++;
                }
            }
        }
    }


    private int getBlockCountByCategory(Category category) {
        switch (category) {
            case DAILY:
                return 1;
            case HOBBY:
                return 2;
            case SCHOOL:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
}
