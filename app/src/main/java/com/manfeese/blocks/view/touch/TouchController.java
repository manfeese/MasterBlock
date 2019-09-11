package com.manfeese.blocks.view.touch;

import android.content.Context;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.manfeese.blocks.view.BlocksView;
import com.manfeese.blocks.view.utils.CoordinateUtils;

public class TouchController implements GestureDetector.OnGestureListener{

    private static final float SENSITIVITY = 0.75f;

    private static final int NO_DIRECTION = 0;
    private static final int X_DIRECTION = 1;
    private static final int Y_DIRECTION = 2;

    private BlocksView view;
    private TouchListener touchListener;
    private GestureDetector gestureDetector;

    private float distanceX, distanceY;

    private int direction;
    private double directionAngle;


    public TouchController(@NonNull Context context, @NonNull BlocksView view, @NonNull TouchListener touchListener) {

        this.view = view;
        this.touchListener = touchListener;

        gestureDetector = new GestureDetector(context, this);

    }


    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {

        distanceX = 0;
        distanceY = 0;

        direction = NO_DIRECTION;

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {

        setDirectionOnScroll(distanceX, distanceY);

        this.distanceX -= distanceX;
        this.distanceY -= distanceY;

        Pair<Integer, Integer> position = getDirectionPosition(view,
                this.distanceX,
                this.distanceY);

        int rows = position.first;
        int columns = position.second;

        if (columns != 0 || rows != 0) {

            if (columns != 0) {
                this.distanceX -= CoordinateUtils.getCoordinate(view, columns);
            } else {
                this.distanceX = 0;
            }

            if (rows != 0) {
                this.distanceY -= CoordinateUtils.getCoordinate(view, rows);
            } else {
                this.distanceY = 0;
            }

            touchListener.onMove(rows, columns, true);

        }

        return true;

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {

        final float distanceTimeFactor = 0.15f;
        final float totalDx = (distanceTimeFactor * velocityX);
        final float totalDy = (distanceTimeFactor * velocityY);

        Pair<Integer, Integer> position = getDirectionPosition(view, totalDx, totalDy);

        int rows = position.first;
        int columns = position.second;

        if (columns != 0 || rows != 0) {
            touchListener.onMove(rows, columns, false);
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        touchListener.onRotate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }



    private Pair<Integer, Integer> getDirectionPosition(@NonNull BlocksView view, float distanceX, float distanceY) {

        int columns = CoordinateUtils.getPosition(view, distanceX * SENSITIVITY);
        int rows = CoordinateUtils.getPosition(view, distanceY * SENSITIVITY);

        /*
         * can't move on dimension-X and dimension-Y
         * at the same time
         * */

        switch (direction) {

            case X_DIRECTION:
                rows = 0;
                break;

            case Y_DIRECTION:
                columns = 0;
                break;

            default:
                rows = 0;
                columns = 0;
                break;

        }

        return new Pair<>(rows, columns);

    }

    private void setDirectionOnScroll(float distanceX, float distanceY) {

        // angle in degrees
        directionAngle = Math.abs(Math.atan2(distanceY, distanceX) * 180 / Math.PI);

        if (45 < directionAngle && directionAngle < 135) {
            direction = Y_DIRECTION;
        } else {
            direction = X_DIRECTION;
        }

    }

}
