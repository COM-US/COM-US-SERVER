package com.example.comus.domain.block.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.entity.BlockShape;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.global.error.ErrorCode;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Transactional
@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    private static final int BOARD_SIZE = 4; // 판의 크기 (4x4)

    public void save(long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANSWER_NOT_FOUND));
        Answer lastAnswer = answerRepository.findSecondLatestAnswer()
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANSWER_NOT_FOUND));

        Block lastBlock = blockRepository.findTopByAnswerIdOrderByCreatedAtDesc(lastAnswer.getId())
                .orElse(null);

        Question question = questionRepository.findById(answer.getQuestion().getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        int blockCount = getBlockCountByCategory(question.getCategory());
        BlockShape shape = getBlockShapeForCount(blockCount);
        shape = getRandomRotation(shape);

        // 블록의 시작 위치를 설정
        int startingLevel = lastBlock != null ? lastBlock.getLevel() : 1;
        int startingRow = lastBlock != null ? lastBlock.getBlockRow() : 0;
        int startingColumn = lastBlock != null ? lastBlock.getBlockColumn() : 0;

        // 모든 블록을 저장하기 위해 블록의 위치를 추적
        Set<String> occupiedPositions = new HashSet<>();
        List<Block> existingBlocks = blockRepository.findAll();
        for (Block block : existingBlocks) {
            occupiedPositions.add(block.getLevel() + "," + block.getBlockRow() + "," + block.getBlockColumn());
        }

        // 블록을 채우기 위한 리스트
        List<int[]> positionsToFill = new ArrayList<>();

        // 블록의 위치를 조정하여 겹치지 않도록 함
        boolean placedSuccessfully = placeBlockWithAdjacency(shape, startingLevel, startingRow, startingColumn, occupiedPositions, positionsToFill);

        if (!placedSuccessfully) {
            throw new RuntimeException("Failed to place the block in the current level.");
        }

        // 기존 판을 채우고 새 판으로 넘어가는 로직
        int remainingBlocks = blockCount;
        while (remainingBlocks > 0) {
            List<int[]> currentBoardPositions = getAvailablePositionsForLevel(startingLevel, occupiedPositions);

            int blocksToPlace = Math.min(remainingBlocks, currentBoardPositions.size());

            for (int i = 0; i < blocksToPlace; i++) {
                int[] position = currentBoardPositions.get(i);
                Block block = Block.builder()
                        .level(position[0])
                        .blockRow(position[1])
                        .blockColumn(position[2])
                        .answer(answer)
                        .build();
                blockRepository.save(block);
            }

            remainingBlocks -= blocksToPlace;

            // 다음 판으로 넘어가기
            startingLevel++;
        }
    }
    private boolean placeBlockWithAdjacency(BlockShape shape, int startingLevel, int startingRow, int startingColumn, Set<String> occupiedPositions, List<int[]> positionsToFill) {
        int[][] shapeOffsets = shape.getShape(); // 블록 모양의 오프셋

        // 블록의 위치를 위한 오프셋 계산
        List<int[]> blockPositions = new ArrayList<>();
        boolean foundPlacement = false;

        for (int[] offset : shapeOffsets) {
            int rowOffset = offset[0];
            int columnOffset = offset[1];

            int level = startingLevel;
            int row = startingRow + rowOffset;
            int column = startingColumn + columnOffset;

            // 위치 조정 및 유효성 검사
            while (row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE || occupiedPositions.contains(level + "," + row + "," + column)) {
                // 주변 빈 공간을 탐색하여 위치를 찾기
                List<int[]> adjacentPositions = getAdjacentAvailablePositions(level, row, column, occupiedPositions);
                if (adjacentPositions.isEmpty()) {
                    return false; // 주변 빈 공간이 없음
                }

                int[] newPos = adjacentPositions.get(0); // 첫 번째 빈 공간을 선택
                level = newPos[0];
                row = newPos[1];
                column = newPos[2];

                foundPlacement = true;
            }

            if (foundPlacement) {
                blockPositions.add(new int[]{level, row, column});
            }
        }

        // 블록의 모든 조각 위치를 occupiedPositions에 추가
        for (int[] pos : blockPositions) {
            occupiedPositions.add(pos[0] + "," + pos[1] + "," + pos[2]);
        }

        // 블록 조각들을 positionsToFill에 추가
        positionsToFill.addAll(blockPositions);

        return true; // 성공
    }

    private List<int[]> getAdjacentAvailablePositions(int level, int row, int column, Set<String> occupiedPositions) {
        List<int[]> adjacentPositions = new ArrayList<>();

        // 가능한 인접 위치 탐색
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1} // 상, 하, 좌, 우
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            if (newRow >= 0 && newRow < BOARD_SIZE && newColumn >= 0 && newColumn < BOARD_SIZE &&
                    !occupiedPositions.contains(level + "," + newRow + "," + newColumn)) {
                adjacentPositions.add(new int[]{level, newRow, newColumn});
            }
        }

        return adjacentPositions;
    }



    private boolean placeBlock(BlockShape shape, int startingLevel, int startingRow, int startingColumn, Set<String> occupiedPositions, List<int[]> positionsToFill) {
        int[][] shapeOffsets = shape.getShape(); // 블록 모양의 오프셋

        // 블록의 위치를 위한 오프셋 계산
        List<int[]> blockPositions = new ArrayList<>();
        for (int[] offset : shapeOffsets) {
            int rowOffset = offset[0];
            int columnOffset = offset[1];

            int level = startingLevel;
            int row = startingRow + rowOffset;
            int column = startingColumn + columnOffset;

            // 위치 조정 및 유효성 검사
            if (row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE || occupiedPositions.contains(level + "," + row + "," + column)) {
                return false; // 블록을 현재 위치에 배치할 수 없음
            }

            blockPositions.add(new int[]{level, row, column});
        }

        // 모든 조각이 유효한 위치에 있는지 확인
        for (int[] pos : blockPositions) {
            int level = pos[0];
            int row = pos[1];
            int column = pos[2];

            if (row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE || occupiedPositions.contains(level + "," + row + "," + column)) {
                return false; // 유효하지 않은 위치 발견
            }
        }

        // 블록의 모든 조각 위치를 occupiedPositions에 추가
        for (int[] pos : blockPositions) {
            occupiedPositions.add(pos[0] + "," + pos[1] + "," + pos[2]);
        }

        // 블록 조각들을 positionsToFill에 추가
        positionsToFill.addAll(blockPositions);

        return true; // 성공
    }


    private List<int[]> getAvailablePositionsForLevel(int level, Set<String> occupiedPositions) {
        List<int[]> availablePositions = new LinkedList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                if (!occupiedPositions.contains(level + "," + row + "," + column)) {
                    availablePositions.add(new int[]{level, row, column});
                }
            }
        }
        return availablePositions;
    }

    private BlockShape getBlockShapeForCount(int blockCount) {
        return switch (blockCount) {
            case 1 -> BlockShape.SINGLE;
            case 2 -> BlockShape.LINE;
            case 3 -> BlockShape.L_SHAPE;
            default -> throw new IllegalArgumentException("Invalid block count: " + blockCount);
        };
    }

    private BlockShape getRandomRotation(BlockShape shape) {
        Random rand = new Random();
        int rotation = rand.nextInt(4);
        BlockShape rotatedShape = shape;
        for (int i = 0; i < rotation; i++) {
            rotatedShape = rotatedShape.rotate();
        }
        return rotatedShape;
    }

    private int getBlockCountByCategory(Category category) {
        return switch (category) {
            case DAILY -> 1;
            case HOBBY -> 2;
            case SCHOOL -> 3;
            default -> 1;
        };
    }
}
