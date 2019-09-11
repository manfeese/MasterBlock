package com.manfeese.blocks.game.data;

import com.manfeese.blocks.game.data.figures.Figure;

import java.util.ArrayList;
import java.util.List;

public class GameField extends SquareBlock {

    public void copyFigureToGameField(Figure figure) {

        int figureRow = figure.getPosition().getRow();
        int figureColumn = figure.getPosition().getColumn();

        for (int row = 0; row < figure.rows; row++) {
            for (int column = 0; column < figure.columns; column++) {

                int destinationRow = figureRow + row;
                int destinationColumn = figureColumn + column;

                if (figure.isDataCell(row, column)) {
                    colorArray[destinationRow][destinationColumn] = figure.getColor(row, column);
                }

            }
        }

    }

    public int[] removeFullLines() {

        int countOfFullLines = countOfFullLines();

        /*
         * contains real numbers of removed lines
         * in reverse order and
         *
         * for example, if fulled lines are 11, 10, 9, 7
         * removed lines will be 11, 11, 11, 10
         * */
        List<Integer> removedLines = new ArrayList<>(countOfFullLines);

        if (countOfFullLines == 0) {
            return new int[0];
        }

        int[][] arrayCopy = new int[rows][columns];
        int row = rows-1;

        while (row >= removedLines.size()) {

            if (isRowFulled(row - removedLines.size())) {

                // init removed line
                for (int column = 0; column < columns; column++) {
                    arrayCopy[removedLines.size()][column] = backgroundColor;
                }

                removedLines.add(row);

            } else {

                arrayCopy[row] = colorArray[row - removedLines.size()];
                row--;

            }

        }

        colorArray = arrayCopy;

        int[] result = new int[removedLines.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = removedLines.get(i);
        }

        return result;

    }

    public int countOfFullLines() {

        int countOfFullLines = 0;

        for (int row = 0; row < rows; row++) {
            if (isRowFulled(row)) {
                countOfFullLines++;
            }
        }

        return countOfFullLines;

    }

    private boolean isRowFulled(int numberOfLine) throws IndexOutOfBoundsException{

        if (numberOfLine < 0 || numberOfLine > rows)
            throw new IndexOutOfBoundsException();

        int countOfTiles = 0;

        for (int column = 0; column < columns; column++) {
            if (colorArray[numberOfLine][column] != backgroundColor) countOfTiles++;
        }

        return (countOfTiles == columns);

    }

}
