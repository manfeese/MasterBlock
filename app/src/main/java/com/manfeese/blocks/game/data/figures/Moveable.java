package com.manfeese.blocks.game.data.figures;

import android.util.Pair;

public interface Moveable {

    int getRowsToMove(int rows);
    int getColumnsToMove(int rows);

    boolean canMove(int rows, int columns);
    void move(int rows, int columns);

}
