package com.manfeese.blocks.view.draw.controller;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.manfeese.blocks.view.data.Blocks;
import com.manfeese.blocks.view.utils.CoordinateUtils;

class MeasureController {

    public static Pair<Integer, Integer> measureViewSize(@NonNull View view, @NonNull Blocks blocks, int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.LayoutParams params = view.getLayoutParams();


        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = (params.width >= 0) ? params.width : View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (params.height >= 0) ? params.height : View.MeasureSpec.getSize(heightMeasureSpec);


        Blocks.StyleParams blocksStyleParams = blocks.getStyleParams();
        float cellSize = CoordinateUtils.getCellSize(blocks.getStyleParams(), heightSize, widthSize);
        blocksStyleParams.setCellSize(cellSize);


        int desiredWidth = (int) CoordinateUtils.getViewCoordinate(blocks.getStyleParams(), CoordinateUtils.ViewCoordinate.WIDTH);
        int desiredHeight = (int) CoordinateUtils.getViewCoordinate(blocks.getStyleParams(), CoordinateUtils.ViewCoordinate.HEIGHT);

        int height;
        int width;

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }


        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }


        width = Math.max(0, width);
        height = Math.max(0, height);


        return new Pair(width, height);

    }

}
