package com.manfeese.blocks.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.manfeese.blocks.view.data.BlocksManager;

public class BlocksView extends View
        //implements TableManager.Listener
{

    private BlocksManager manager;

    public BlocksView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Pair<Integer, Integer> pair = manager.drawer().measureViewSize(this, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(pair.first, pair.second);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        manager.drawer().draw(canvas);

    }

//    @Override
//    public void onTableUpdated() {
//        invalidate();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private void init(@Nullable AttributeSet attrs) {

        manager = new BlocksManager();
        manager.drawer().initAttributes(getContext(), attrs);

    }



}
