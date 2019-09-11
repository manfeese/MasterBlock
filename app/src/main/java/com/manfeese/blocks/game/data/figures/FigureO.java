package com.manfeese.blocks.game.data.figures;

import com.manfeese.blocks.game.data.GameField;

public class FigureO extends Figure {

    FigureO(GameField gameField, int dataColor, int rotationState) {
        super(gameField, dataColor, rotationState);
    }

    @Override
    protected int[][][] getFigureTemplates() {

        return new int[][][] {
                {
                        {4, 4},
                        {4, 4},
                },
        };

    }

    @Override
    public boolean rotate() {
        return true;
    }
}
