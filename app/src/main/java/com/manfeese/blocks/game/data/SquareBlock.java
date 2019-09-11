package com.manfeese.blocks.game.data;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class SquareBlock {

    protected int rows;
    protected int columns;

    @ColorInt
    protected int[][] colorArray;

    @ColorInt
    int backgroundColor;



    public void init() {
        init(null, -1);
    }

    public void init(SquareBlock squareBlock) {
        init(squareBlock, -1);
    }

    public void init(SquareBlock squareBlock, int destinationDataColor) {
        if (squareBlock == null) {
            init(null, 0, destinationDataColor);
        } else {
            init(squareBlock.colorArray, squareBlock.backgroundColor, destinationDataColor);
        }
    }

    protected void init(int[][] sourceArray, int sourceBackgroundColor, int destinationDataColor) {

        if (sourceArray != null) {
            setRows(sourceArray.length);
            setColumns(sourceArray.length == 0 ? 0 : sourceArray[0].length);
        }

        colorArray = new int[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {

                int color = backgroundColor;

                if (sourceArray != null && sourceArray[row][column] != sourceBackgroundColor) {
                    color = destinationDataColor == -1 ? sourceArray[row][column] : destinationDataColor;
                }

                colorArray[row][column] = color;

            }
        }

    }



    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }



    public boolean isDataCell(int row, int column){
        return getColor(row, column) != backgroundColor;
    }

    public int getColor(int row, int column){
        return colorArray[row][column];
    }

    public void setDataColor(int dataColor) {

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {

                if (isDataCell(row, column)) {
                    setColor(row, column, dataColor);
                }

            }
        }

    }

    @Override
    @NonNull
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("[").append(" ");
        for (int i = 0; i < rows; i++) {
            builder.append("[");
            for (int j = 0; j < columns; j++) {

                if (j != 0) {
                    builder.append(", ");
                }
                builder.append(colorArray[i][j]);

            }
            builder.append("]").append(" ");
        }
        builder.append("]");

        return "" + getClass().getSimpleName() + ", "
                + builder.toString();

    }


    private void setColor(int row, int column, int color){
        colorArray[row][column] = color;
    }


}
