package com.manfeese.blocks.view.draw.controller;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;

import com.manfeese.blocks.view.data.Blocks;
import com.manfeese.blocks.view.utils.CoordinateUtils;

public class DrawController {

    Blocks blocks;

    public DrawController(@NonNull Blocks blocks) {
        this.blocks = blocks;
    }

    public Pair<Integer, Integer> measureViewSize(@NonNull View view, @NonNull Blocks blocks, int widthMeasureSpec, int heightMeasureSpec) {
        return MeasureController.measureViewSize(view, blocks, widthMeasureSpec, heightMeasureSpec);
    }


    public void draw(@NonNull Canvas canvas) {

        Blocks.StyleParams styleParams = blocks.getStyleParams();
        int color = styleParams.getColor();

        for (int row = 0; row < styleParams.getRows(); row++) {
            for (int column = 0; column < styleParams.getColumns(); column++) {

                float left = CoordinateUtils.getViewCoordinate(blocks.getStyleParams(), CoordinateUtils.ViewCoordinate.LEFT, column);
                float right = CoordinateUtils.getViewCoordinate(blocks.getStyleParams(), CoordinateUtils.ViewCoordinate.RIGHT, column);
                float top = CoordinateUtils.getViewCoordinate(blocks.getStyleParams(), CoordinateUtils.ViewCoordinate.TOP, row);
                float bottom = CoordinateUtils.getViewCoordinate(blocks.getStyleParams(), CoordinateUtils.ViewCoordinate.BOTTOM, row);

                Paint p = new Paint();
                p.setAntiAlias(true);
                p.setStyle(Paint.Style.FILL);
                p.setColor(color);

                canvas.drawRect(left, top, right, bottom, p);

            }
        }

    }
}
