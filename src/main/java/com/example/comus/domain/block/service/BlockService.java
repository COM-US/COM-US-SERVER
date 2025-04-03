package com.example.comus.domain.block.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.block.dto.request.BlockPlaceRequestDto;
import com.example.comus.domain.block.dto.request.BlockRequestDto;
import com.example.comus.domain.block.dto.response.BlockCountResponseDto;
import com.example.comus.domain.block.dto.response.BlockPlaceResponseDto;
import com.example.comus.domain.block.dto.response.BlockResponseDto;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.global.error.exception.BusinessException;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.comus.global.error.ErrorCode.*;

@AllArgsConstructor
@Transactional
@Service
public class BlockService {

    private final AnswerRepository answerRepository;
    private final BlockRepository blockRepository;
    private final static int MAX_SIZE = 4;


    // 블럭 개수 조회
    public BlockCountResponseDto getBlockCount(Long userId) {
        Map<QuestionCategory, Integer> countMap = Arrays.stream(QuestionCategory.values()).collect(Collectors.toMap(category -> category, category -> {
            Long count = answerRepository.countByUserIdAndQuestionCategoryAndIsUsedFalse(userId, category);
            return count != null ? count.intValue() : 0;
        }));

        return BlockCountResponseDto.of(countMap.get(QuestionCategory.DAILY), countMap.get(QuestionCategory.SCHOOL), countMap.get(QuestionCategory.HOBBY), countMap.get(QuestionCategory.FAMILY), countMap.get(QuestionCategory.FRIEND));
    }

    //블록 조회
    public List<BlockResponseDto> getBlock(Long userId) {

        int currentLevel = blockRepository.findMaxLevelByUserId(userId);
        if (isLevelFull(userId, currentLevel)) {
            return null;
        }

        // 사용자 현재 블록(단계가 가장 높은) 조회 & 같은 답변 정보를 가진 블록 그룹화
        return blockRepository.findMaxLevelBlocksByUserId(userId).stream()
                .collect(Collectors.groupingBy(block -> block.getAnswer().getId()))
                .values().stream()
                .map(this::createBlockResponse)
                .collect(Collectors.toList());
    }

    // 그룹화 된 블록에 대한 답변, 블록 위치 정보 생성
    private BlockResponseDto createBlockResponse(List<Block> blocks) {

        Answer answer = blocks.get(0).getAnswer();
        int level = blocks.get(0).getLevel();

        // 블록 위치 정보
        List<BlockPlaceResponseDto> blockPlaceList = blocks.stream()
                .map(block -> new BlockPlaceResponseDto(block.getId(), block.getBlockRow(), block.getBlockColumn()))
                .collect(Collectors.toList());

        return BlockResponseDto.of(answer, blockPlaceList, level);
    }

    // TODO : 답변의 질문 카테고리에 따라 블록 모형 & 개수 제한
    // 블록 배치
    @Transactional
    public void createBlock(Long userId, BlockRequestDto blockRequest) {

        Answer answer = getUnusedAnswer(userId, blockRequest.category());

        int currentLevel = blockRepository.findMaxLevelByUserId(userId);

        if (isLevelFull(userId, currentLevel)) {currentLevel++;}
        validateBlockPosition(blockRequest.blockPlace(), currentLevel, userId);

        for (BlockPlaceRequestDto place : blockRequest.blockPlace()) {
            Block block = Block.builder()
                    .answer(answer)
                    .blockRow(place.row())
                    .blockColumn(place.column())
                    .level(currentLevel)
                    .build();

            blockRepository.save(block);
        }
        answer.setUsed();
    }

    // 사용하지 않는 답변
    private Answer getUnusedAnswer(Long userId, QuestionCategory questionCategory) {
        List<Answer> answers = answerRepository.findByUserIdAndQuestionCategoryAndIsUsedFalseOrderByCreatedAtAsc(userId, questionCategory);
        if (answers.isEmpty()) {
            throw new BusinessException(BLOCK_CATEGORY_NOT_FOUND);
        }
        return answers.get(0);
    }

    // 블록 위치 유효성 검사
    public void validateBlockPosition(List<BlockPlaceRequestDto> blockPlaces, int currentLevel, Long userId) {
        for (BlockPlaceRequestDto place : blockPlaces) {
            if (place.row() < 0 || place.row() >= MAX_SIZE || place.column() < 0 || place.column() >= MAX_SIZE) {
                throw new BusinessException(BLOCK_POSITION_OUT_OF_BOUNDS);
            }
            boolean positionExists = blockRepository.existsByBlockRowAndBlockColumnAndLevelAndAnswer_UserId(
                    place.row(), place.column(), currentLevel, userId);
            if (positionExists) {
                throw new BusinessException(BLOCK_POSITION_ALREADY_OCCUPIED);
            }
        }
    }

    private boolean isLevelFull(Long userId, int level) {
        int blockCount = blockRepository.countByAnswer_UserIdAndLevel(userId, level);
        return blockCount >= MAX_SIZE * MAX_SIZE;
    }

    public void deleteBlock(Long userId) {
        List<Block> blocks = blockRepository.findByAnswer_UserId(userId);
        for (Block block : blocks) {
            blockRepository.delete(block);
        }
    }
}

