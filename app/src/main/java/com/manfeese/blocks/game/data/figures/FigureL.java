package com.manfeese.blocks.game.data.figures;

import com.manfeese.blocks.game.data.GameField;

public class FigureL extends Figure {

    FigureL(GameField gameField, int dataColor, int rotationState) {
        super(gameField, dataColor, rotationState);
    }

    @Override
    protected int[][][] getFigureTemplates() {

        return new int[][][]{
                {
                        {0, 0, 4},
                        {4, 4, 4}
                },
                {
                        {4, 0},
                        {4, 0},
                        {4, 4},
                },
                {
                        {4, 4, 4},
                        {4, 0, 0}
                },
                {
                        {4, 4},
                        {0, 4},
                        {0, 4},
                },
        };
    }

    @Override
    protected void moveAfterRotate(int prevRotationState) {

        int[][] positionState = new int[][] {
                {0,0,},
                {0,1,},
                {1,0,},
                {0,0,},
        };

        int prevRow = positionState[prevRotationState][0];
        int prevColumn = positionState[prevRotationState][1];

        int newRow = positionState[rotationState][0];
        int newColumn = positionState[rotationState][1];

        if (prevRow != newRow || prevColumn != newColumn) {
            move(newRow - prevRow, newColumn - prevColumn);
        }

        super.moveAfterRotate(prevRotationState);

    }
}
