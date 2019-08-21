package com.manfeese.blocks.view.draw.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.manfeese.blocks.view.data.Blocks;
import com.manfeese.blocks.view.draw.controller.AttributesController;
import com.manfeese.blocks.view.draw.controller.DrawController;

public class DrawManager {

    private Blocks blocks;
    private DrawController drawController;
    private AttributesController attributesController;

    public DrawManager() {
        blocks = new Blocks();
        drawController = new DrawController(blocks);
        attributesController = new AttributesController(blocks);
    }

    public void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        attributesController.init(context, attrs);
    }

    public Pair<Integer, Integer> measureViewSize(@NonNull View view, int widthMeasureSpec, int heightMeasureSpec) {
        return drawController.measureViewSize(view, blocks, widthMeasureSpec, heightMeasureSpec);
    }

    public void draw(@NonNull Canvas canvas) {
        drawController.draw(canvas);
    }

}
