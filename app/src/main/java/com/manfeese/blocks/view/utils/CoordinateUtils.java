package com.manfeese.blocks.view.utils;

import androidx.annotation.NonNull;

import com.manfeese.blocks.view.data.Blocks;

public class CoordinateUtils {

    public static float getViewCoordinate(@NonNull Blocks.StyleParams params, @NonNull ViewCoordinate measure) {

        int measureValue;

        switch (measure) {

            case WIDTH:
                measureValue = params.getColumns();
                break;

            case HEIGHT:
                measureValue =params.getRows();
                break;

            default:
                measureValue = 0;

        }

        return getViewCoordinate(params, measure, measureValue);

    }

    public static float getViewCoordinate(@NonNull Blocks.StyleParams params, @NonNull ViewCoordinate measure, int measureValue) {

        float result;

        switch (measure) {

            case WIDTH:
            case HEIGHT:
            case LEFT:
            case TOP:
                result = getCoordinate(params, measureValue) + params.getMargin();
                break;

            case RIGHT:
            case BOTTOM:
                result = getCoordinate(params, measureValue + 1);
                break;

            default:
                result = 0.0f;
        }

        return result;

    }

    private static float getViewCoordinate(@NonNull Blocks.StyleParams params, int index) {

        float cellSize = params.getCellSize();
        float margin = params.getMargin();

        return index * (cellSize + margin) + margin;

    }

    public static float getCoordinate(@NonNull Blocks.StyleParams params, int index) {

        float cellSize = params.getCellSize();
        float margin = params.getMargin();

        return index * (cellSize + margin);

    }

    public static int getPosition(@NonNull Blocks.StyleParams params, float coordinate) {

        float cellSize = params.getCellSize();
        float margin = params.getMargin();

        return (int) ((coordinate - margin) / (cellSize + margin));

    }

    public static float getCellSize(@NonNull Blocks.StyleParams params, float height, float width) {

        float margin = params.getMargin();
        float rows = params.getRows();
        float columns = params.getColumns();

        float cellHeight = (height - margin) / rows - margin;
        float cellWidth = (width - margin) / columns - margin;

        return Math.min(cellWidth, cellHeight);

    }

    public enum ViewCoordinate {
        WIDTH,
        HEIGHT,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

}
