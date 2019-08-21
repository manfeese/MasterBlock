package com.example.t3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class GridView extends View {

    private int gridColumns;
    private int gridRows;
    private int gridWidth;

    private float cellSize;
//    private int width;
//    private int height;

    public GridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GridView);
        gridColumns = typedArray.getInt(R.styleable.GridView_grid_сolumns, 0);
        gridRows = typedArray.getInt(R.styleable.GridView_grid_rows, 0);
        gridWidth = typedArray.getInt(R.styleable.GridView_grid_width, 0);
        typedArray.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = getLayoutParams();

        int height;
        int width;

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = (params.width >= 0) ? params.width : View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (params.height >= 0) ? params.height : View.MeasureSpec.getSize(heightMeasureSpec);


        float cellHeight = (float) (heightSize - gridWidth) / gridRows - gridWidth;
        float cellWidth = (float) (widthSize - gridWidth) / gridColumns - gridWidth;
        cellSize = Math.min(cellWidth, cellHeight);

        int desiredWidth = (int) (gridColumns * (cellSize + gridWidth) + gridWidth);
        int desiredHeight = (int) (gridRows * (cellSize + gridWidth) + gridWidth);

//        int desiredWidth = (int) (gridColumns * (cellSize + gridWidth) + gridWidth);
//        int desiredHeight = (int) (gridRows * (cellSize + gr