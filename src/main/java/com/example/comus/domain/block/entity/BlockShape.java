package com.example.comus.domain.block.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockShape {
    SINGLE(new int[][]{{0, 0}}),

    LINE(new int[][]{{0, 0}, {1, 0}}),
    LINE_90(new int[][]{{0, 0}, {0, 1}}),

    L_SHAPE(new int[][]{{0, 0}, {1, 0}, {1, 1}}),
    L_SHAPE_90(new int[][]{{0, 0}, {0, 1}, {1, 0}}),
    L_SHAPE_180(new int[][]{{0, 0}, {0, 1}, {-1, 1}}),
    L_SHAPE_270(new int[][]{{0, 0}, {-1, 0}, {-1, 1}});

    private final int[][] shape;

    public BlockShape rotate() {
        switch (this) {
            case SINGLE:
                return SINGLE;
            case LINE:
                return LINE_90;
            case LINE_90:
                return LINE;
            case L_SHAPE:
                return L_SHAPE_90;
            case L_SHAPE_90:
                return L_SHAPE_180;
            case L_SHAPE_180:
                return L_SHAPE_270;
            case L_SHAPE_270:
                return L_SHAPE;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
