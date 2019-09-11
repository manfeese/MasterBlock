package com.manfeese.blocks.game.data.figures;

import com.manfeese.blocks.game.data.GameField;

public class EmptyFigure extends Figure {

    public EmptyFigure(GameField gameField) {
        super(gameField);
    }

    @Override
    protected int[][][] getFigureTemplates() {
        return null;
    }

    @Override
    public boolean canMove(int rows, int columns) {
        return false;
    }

}
