package com.manfeese.blocks.view.touch;

public interface TouchListener {

    void onMove(int rows, int columns, boolean isInteractive);
    void onRotate();

}
