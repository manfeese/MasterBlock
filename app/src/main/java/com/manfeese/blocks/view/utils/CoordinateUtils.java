package com.manfeese.blocks.view.utils;

import androidx.annotation.NonNull;

import com.manfeese.blocks.view.BlocksView;

public class CoordinateUtils {

    public static float getViewCoordinate(@NonNull BlocksView view, @NonNull ViewCoordinate measure) {

        int measureValue;

        switch (measure) {

            case WIDTH:
                measureValue = view.getBlock().getColumns();
                break;

            case HEIGHT:
                measureValue = view.getBlock().getRows();
                break;

            default:
                measureValue = 0;

        }

        return getViewCoordinate(view, measure, measureValue);

    }

    public static float getViewCoordinate(@NonNull BlocksView view, @NonNull ViewCoordinate measure, int measureValue) {

        float result;

        switch (measure) {

            case WIDTH:
            case HEIGHT:
            case LEFT:
            case TOP:
                result = getCoordinate(view, measureValue) + view.getMargin();
                break;

            case RIGHT:
            case BOTTOM:
                result = getCoordinate(view, measureValue + 1);
                break;

            default:
                result = 0.0f;
        }

        return result;

    }


    public static float getCoordinate(@NonNull BlocksView view, int index) {

        float cellSize = view.getCellSize();
        float margin = view.getMargin();

        return index * (cellSize + margin);

    }

    public static int getPosition(@NonNull BlocksView view, float coordinate) {

        float cellSize = view.getCellSize();
        float margin = view.getMargin();

        return (int) ((coordinate - margin) / (cellSize));

    }

    public static float getCellSize(@NonNull BlocksView view, float height, float width) {

        float margin = view.getMargin();
        float rows = view.getBlock().getRows();
        float columns = view.getBlock().getColumns();

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
