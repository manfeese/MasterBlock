package com.manfeese.blocks.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.manfeese.blocks.game.data.SquareBlock;
import com.manfeese.blocks.view.touch.TouchController;
import com.manfeese.blocks.view.touch.TouchListener;
import com.manfeese.blocks.view.utils.CoordinateUtils;
import com.manfeese.squareBlocks.R;

public class BlocksView extends View {

    private SquareBlock block;
    private Paint paint;
    private TouchController touchController;

    protected float cellSize;
    protected float margin;
    protected boolean scaled;
    protected int sizeOf = View.NO_ID;

    public BlocksView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.LayoutParams params = getLayoutParams();

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = (params.width >= 0) ? params.width : View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (params.height >= 0) ? params.height : View.MeasureSpec.getSize(heightMeasureSpec);


        int sizeResID = getSizeOf();
        if (sizeResID != View.NO_ID) {

            View view = ((Activity) getContext()).findViewById(sizeResID);
            if (view instanceof BlocksView) {

                BlocksView blocksView = (BlocksView) view;
                setCellSize(blocksView.getCellSize());
                setMargin(blocksView.getMargin());
                setScaled(false);

            }

        }

        if (isScaled()) {

            float cellSize = CoordinateUtils.getCellSize(this, heightSize, widthSize);
            setCellSize(cellSize);

        }


        int desiredWidth = Math.round(CoordinateUtils.getViewCoordinate(this, CoordinateUtils.ViewCoordinate.WIDTH));
        int desiredHeight = Math.round(CoordinateUtils.getViewCoordinate(this, CoordinateUtils.ViewCoordinate.HEIGHT));

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

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int row = 0; row < block.getRows(); row++) {
            for (int column = 0; column < block.getColumns(); column++) {

                float left = CoordinateUtils.getViewCoordinate(this, CoordinateUtils.ViewCoordinate.LEFT, column);
                float right = CoordinateUtils.getViewCoordinate(this, CoordinateUtils.ViewCoordinate.RIGHT, column);
                float top = CoordinateUtils.getViewCoordinate(this, CoordinateUtils.ViewCoordinate.TOP, row);
                float bottom = CoordinateUtils.getViewCoordinate(this, CoordinateUtils.ViewCoordinate.BOTTOM, row);
                paint.setColor(block.getColor(row, column));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(left, top, right, bottom, margin+1, margin+1, paint);
                } else {
                    canvas.drawRect(left, top, right, bottom, paint);
                }

            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (touchController != null) {
            return touchController.onTouchEvent(event);
        }

        return super.onTouchEvent(event);

    }

    public void setOnTouchListener(TouchListener onTouchListener) {

        touchController = new TouchController(getContext(), this, onTouchListener);

    }


    public SquareBlock getBlock() {
        return block;
    }

    public void setBlock(SquareBlock block) {

        this.block = block;

        requestLayout();
        invalidate();

    }


    public float getCellSize() {
        return cellSize;
    }

    public float getMargin() {
        return margin;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public boolean isScaled() {
        return scaled;
    }

    public void setScaled(boolean scaled) {
        this.scaled = scaled;
    }

    public int getSizeOf() {
        return sizeOf;
    }

    public void setSizeOf(int resID) {
        this.sizeOf = resID;
    }


    private void init(@Nullable AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BlocksView);

        int rows = typedArray.getInt(R.styleable.BlocksView_bv_rows, 0);
        int columns = typedArray.getInt(R.styleable.BlocksView_bv_columns, 0);
        int backgroundColor = typedArray.getInt(R.styleable.BlocksView_bv_color, Color.TRANSPARENT);

        int sizeOf = typedArray.getResourceId(R.styleable.BlocksView_bv_sizeOf, View.NO_ID);
        boolean scaled = typedArray.getBoolean(R.styleable.BlocksView_bv_scaled, true);
        float cellSize = typedArray.getFloat(R.styleable.BlocksView_bv_cellSize, 0.0f);
        float margin = typedArray.getFloat(R.styleable.BlocksView_bv_margin, 0.0f);

        typedArray.recycle();

        init(rows, columns, backgroundColor, sizeOf, scaled, cellSize, margin);


    }

    private void init(int rows, int columns, int backgroundColor, int sizeOf, boolean scaled, float cellSize, float margin) {

        setSizeOf(sizeOf);
        setScaled(scaled);
        setCellSize(cellSize);
        setMargin(margin);

        block = new SquareBlock();
        block.setRows(rows);
        block.setColumns(columns);
        block.setBackgroundColor(backgroundColor);
        block.init();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

    }

}
