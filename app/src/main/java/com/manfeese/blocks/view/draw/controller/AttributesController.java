package com.manfeese.blocks.view.draw.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.manfeese.blocks.view.data.Blocks;
import com.manfeese.squareBlocks.R;

public class AttributesController {

    Blocks blocks;

    public AttributesController(@NonNull Blocks blocks) {
        this.blocks = blocks;
    }

    public void init(@NonNull Context context, @Nullable AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlocksView);
        initColorAttribute(typedArray);
        initSizeAttribute(typedArray);
        typedArray.recycle();

        blocks.init();

    }

    private void initSizeAttribute(TypedArray typedArray) {

        int rows = typedArray.getInt(R.styleable.BlocksView_rows, 0);
        int columns = typedArray.getInt(R.styleable.BlocksView_columns, 0);
        int margin = typedArray.getInt(R.styleable.BlocksView_margin, 0);

        Blocks.StyleParams styleParams = blocks.getStyleParams();
        styleParams.setRows(rows);
        styleParams.setColumns(columns);
        styleParams.setMargin(margin);


    }

    private void initColorAttribute(TypedArray typedArray) {

        int color = typedArray.getInt(R.styleable.BlocksView_color, 0);

        Blocks.StyleParams styleParams = blocks.getStyleParams();
        styleParams.setColor(color);

    }
}
