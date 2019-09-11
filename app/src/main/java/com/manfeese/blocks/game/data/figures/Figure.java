package com.manfeese.blocks.game.data.figures;

import android.graphics.Color;

import com.manfeese.blocks.game.data.GameField;
import com.manfeese.blocks.game.data.Position;
import com.manfeese.blocks.game.data.SquareBlock;

public abstract class Figure extends SquareBlock implements Moveable, Rotateable {

    protected GameField gameField;
    private Position position;

    protected int rotationState;
    protected int dataColor;


    public Figure(GameField gameField) {
        this(gameField, Color.TRANSPARENT);
    }

    public Figure(GameField gameField, int dataColor) {
        this(gameField, dataColor, 0);
    }

    public Figure(GameField gameField, int dataColor, int rotationState) {

        this.gameField = gameField;
        this.dataColor = dataColor;

        setRotationState(rotationState);
        initState();
        setPosition(0, 0);

    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(int rows, int columns) {

        position = new Position(
                gameField,
                this,
                rows,
                columns);

    }


    @Override
    public boolean rotate() {

        int degree = getRotationDegree();
        if (degree == 0) {
            return false;
        }

        // save current state of figure
        int rows = this.rows;
        int columns = this.columns;
        int[][] colorArray = this.colorArray;
        Position position = this.position;
        int rotationState = this.rotationState;

        // next rotation state
        int rotationChange = this.rotationState + degree > 0 ? 1 : -1;
        setRotationState(this.rotationState + rotationChange);
        if (this.rotationState == rotationState) {
            return false;
        }

        // next figure state
        initState();
        setPosition(position.getRow(), position.getColumn());

        // set new figure position
        moveAfterRotate(rotationState);

        // if figure rotated succesfully
        // set the rotation
        if (this.position.isOverlay()) {

            // figure didn't rotate succesfully
            // restore previous state
            this.rows = rows;
            this.columns = columns;
            this.colorArray = colorArray;
            this.position = position;
            this.rotationState = rotationState;

            return false;

        }

        return true;
    }

    @Override
    public boolean canMove(int rows, int columns) {

        if (rows == 0 && columns == 0) {
            return false;
        }

        int newRow = position.getRow() + rows;
        int newColumn = position.getColumn() + columns;

        int maxRow = position.getMaxRow();
        int minColumn = position.getMinColumn();
        int maxColumn = position.getMaxColumn();

        return newRow <= maxRow
                && newColumn >= minColumn
                && newColumn <= maxColumn;
    }

    @Override
    public void move(int rows, int columns) {

        int newRow = position.getRow() + rows;
        int newColumn = position.getColumn() + columns;

        setPosition(newRow, newColumn);

    }

    @Override
    public int getRowsToMove(int rows) {

        int currentRow = position.getRow();
        int newRow = currentRow + rows;

        int minRow = position.getMinRow();
        int maxRow = position.getMaxRow();

        if (newRow < minRow) {
            newRow = minRow;
        }

        if (newRow > maxRow) {
            newRow = maxRow;
        }

        return newRow - currentRow;
    }

    @Override
    public int getColumnsToMove(int columns) {

        int currentColumn = position.getColumn();
        int newColumn = currentColumn + columns;

        int minColumn = position.getMinColumn();
        int maxColumn = position.getMaxColumn();

        if (newColumn < minColumn) {
            newColumn = minColumn;
        }

        if (newColumn > maxColumn) {
            newColumn = maxColumn;
        }

        return newColumn - currentColumn;
    }

    public int getRotationDegree() {
        return 90;
    }

    protected abstract int[][][] getFigureTemplates();

    protected void moveAfterRotate(int prevRotationState) {

        int rowsToMove = getRowsToMove(0);
        int columnsToMove = getColumnsToMove(0);
        if (canMove(rowsToMove, columnsToMove)) {
            move(rowsToMove, columnsToMove);
        }

    }



    private void initState() {

        int[][] colorArray = null;

        int[][][] figureTemplates = getFigureTemplates();
        if (figureTemplates != null && figureTemplates.length > 0) {
            colorArray = figureTemplates[rotationState];
        }

        init(colorArray, 0, dataColor);
    }

    private void setRotationState(int rotationState) {

        this.rotationState = 0;

        int[][][] figureTemplates = getFigureTemplates();
        if (figureTemplates != null && figureTemplates.length > 0) {
            int rotationCount = figureTemplates.length;
            this.rotationState = (rotationCount + rotationState % rotationCount) % rotationCount;
        }

    }


}
