package com.example.comus.domain.block.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.block.dto.response.BlockCountResponseDto;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.entity.BlockShape;
import com.example.comus.domain.block.repository.BlockRepository;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.global.error.ErrorCode;
import com.example.comus.global.error.exception.EntityNotFoundException;
import com.example.comus.global.error.exception.InvalidValueException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    private static final int BOARD_SIZE = 4;
    private static final int MAX_BLOCKS_PER_LEVEL = BOARD_SIZE * BOARD_SIZE;

    // 블럭 개수 조회
    public BlockCountResponseDto getBlockCount(Long userId) {
        Map<QuestionCategory, Integer> countMap = Arrays.stream(QuestionCategory.values())
                .collect(Collectors.toMap(
                        category -> category,
                        category -> {
                            Long count = answerRepository.countByUserIdAndQuestionCategoryAndIsUsedFalse(userId, category);
                            return count != null ? count.intValue() : 0;
                        }
                ));

        return BlockCountResponseDto.of(
                countMap.get(QuestionCategory.DAILY),
                countMap.get(QuestionCategory.SCHOOL),
                countMap.get(QuestionCategory.HOBBY),
                countMap.get(QuestionCategory.FAMILY),
                countMap.get(QuestionCategory.FRIEND)
        );
    }

    public void save(long userId, long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANSWER_NOT_FOUND));
        Question question = questionRepository.findById(answer.getQuestion().getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        List<Answer> answers = answerRepository.findByUserId(userId);
        List<Block> blocks = blockRepository.findByAnswerIn(answers);

        int blockCount = getBlockCountByCategory(question.getCategory());
        BlockShape shape = getBlockShapeForCount(blockCount);
        shape = getRandomRotation(shape);

        int startingLevel = 1;

        Map<Integer, Set<String>> occupiedPositionsByLevel = new HashMap<>();
        List<Block> existingBlocks = blockRepository.findAll();
        for (Block block : existingBlocks) {
            occupiedPositionsByLevel
                    .computeIfAbsent(block.getLevel(), k -> new HashSet<>())
                    .add(getPositionKey(block));
        }

        int remainingBlocks = blockCount;
        boolean blocksPlaced = false;

        while (remainingBlocks > 0) {
            Set<String> occupiedPositions = occupiedPositionsByLevel.computeIfAbsent(startingLevel, k -> new HashSet<>());
            List<int[]> availablePositions = getAvailablePositionsForLevel(startingLevel, occupiedPositions);

            availablePositions.sort(Comparator.comparingInt(a -> a[1] * BOARD_SIZE + a[2]));

            if (availablePositions.isEmpty()) {
                if (occupiedPositions.size() >= MAX_BLOCKS_PER_LEVEL) {
                    startingLevel++;
                    continue;
                } else {
                    throw new InvalidValueException(ErrorCode.BLOCK_IS_INAPPROPRIATE);
                }
            }

            List<int[]> positionsToFill = new ArrayList<>();
            boolean placedSuccessfully = false;

            for (int[] position : availablePositions) {
                int row = position[1];
                int column = position[2];

                if (placeBlockWithAdjacency(shape, startingLevel, row, column, occupiedPositions, positionsToFill)) {
                    placedSuccessfully = true;
                    remainingBlocks -= positionsToFill.size();
                    blocksPlaced = true;
                    break;
                }
            }

            if (placedSuccessfully) {
                saveBlocks(positionsToFill, answer);
            } else {
                if (occupiedPositions.size() >= MAX_BLOCKS_PER_LEVEL) {
                    startingLevel++;
                } else {
                    throw new InvalidValueException(ErrorCode.BLOCK_IS_INAPPROPRIATE);
                }
            }
        }

        if (!blocksPlaced) {
            throw new InvalidValueException(ErrorCode.BLOCK_IS_INAPPROPRIATE);
        }
    }

    private boolean placeBlockWithAdjacency(BlockShape shape, int level, int startingRow, int startingColumn, Set<String> occupiedPositions, List<int[]> positionsToFill) {
        int[][] shapeOffsets = shape.getShape();
        List<int[]> blockPositions = new ArrayList<>();

        for (int[] offset : shapeOffsets) {
            int rowOffset = offset[0];
            int columnOffset = offset[1];

            int row = startingRow + rowOffset;
            int column = startingColumn + columnOffset;

            if (isPositionValid(row, column) && !isPositionOccupied(level, row, column, occupiedPositions)) {
                blockPositions.add(new int[]{level, row, column});
            } else {
                return false;
            }
        }

        for (int[] pos : blockPositions) {
            occupiedPositions.add(getPositionKey(pos));
        }

        positionsToFill.addAll(blockPositions);

        return true;
    }

    private void saveBlocks(List<int[]> positionsToFill, Answer answer) {
        for (int[] position : positionsToFill) {
            Block block = Block.builder()
                    .level(position[0])
                    .blockRow(position[1])
                    .blockColumn(position[2])
                    .answer(answer)
                    .build();
            blockRepository.save(block);
        }
    }

    private List<int[]> getAvailablePositionsForLevel(int level, Set<String> occupiedPositions) {
        List<int[]> availablePositions = new LinkedList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                if (!occupiedPositions.contains(getPositionKey(level, row, column))) {
                    availablePositions.add(new int[]{level, row, column});
                }
            }
        }
        return availablePositions;
    }

    private String getPositionKey(int level, int row, int column) {
        return level + "," + row + "," + column;
    }

    private String getPositionKey(int[] pos) {
        return getPositionKey(pos[0], pos[1], pos[2]);
    }

    private String getPositionKey(Block block) {
        return getPositionKey(block.getLevel(), block.getBlockRow(), block.getBlockColumn());
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

    private int getBlockCountByCategory(QuestionCategory category) {
        return switch (category) {
            case DAILY -> 1;
            case HOBBY -> 2;
            case SCHOOL -> 3;
            default -> 1;
        };
    }

    private boolean isPositionValid(int row, int column) {
        return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
    }

    private boolean isPositionOccupied(int level, int row, int column, Set<String> occupiedPositions) {
        return occupiedPositions.contains(getPositionKey(level, row, column));
    }
}
