
package com.manfeese.blocks.game.data;

public class Position {

    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 4;
    private static final int BOTTOM = 8;
    private static final int ALL = 15;

    private int row;
    private int column;

    private int minRow;
    private int maxRow;
    private int minColumn;
    private int maxColumn;

    private boolean overlay;


    public Position(SquareBlock gameField, SquareBlock figure) {
        this(gameField, figure, 0, 0);
    }

    public Position(SquareBlock gameField, SquareBlock figure, int row, int column) {
        this.row = row;
        this.column = column;
        calculateBorder(gameField, figure, ALL);
        checkOverlay(gameField, figure);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getMinRow() {
        return minRow;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getMinColumn() {
        return minColumn;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public boolean isOverlay() {
        return overlay;
    }


    private void checkOverlay(SquareBlock gameField, SquareBlock figure) {

        for (int rowIndex = 0; rowIndex < figure.getRows(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < figure.getColumns(); columnIndex++) {

                if (!figure.isDataCell(rowIndex, columnIndex)) {
                    continue;
                }

                int gameFieldRow = row + rowIndex;
                int gameFieldColumn = column + columnIndex;

                if (gameFieldRow < 0
                        || gameFieldRow >= gameField.getRows()
                        || gameFieldColumn < 0
                        || gameFieldColumn >= gameField.getColumns()
                        || gameField.isDataCell(gameFieldRow, gameFieldColumn)) {
                    overlay = true;
                    return;
                }

            }
        }

        overlay = false;

    }

    private void calculateBorder(SquareBlock gameField, SquareBlock figure, int border) {

        int rows = figure.getRows();
        int columns = figure.getColumns();

        if ((border & LEFT) == LEFT) {

            if (rows == 0) {

                minColumn = 0;

            } else {

                int countOfEmptyTiles = gameField.getRows() - 1;
                for (int rowIndex = 0; rowIndex < rows; rowIndex++) {

                    // only rows rowIndex
                    int figureFromRow = rowIndex;
                    int figureToRow = rowIndex + 1;

                    // all columns of figure
                    int figureFromColumn = 0;
                    int figureToColumn = columns;

                    int figureEmptyTiles = getCountOfEmptyTiles(
                            figure,
                            figureFromRow,
                            figureToRow,
                            figureFromColumn,
                            figureToColumn,
                            true);


                    // only rows rowIndex
                    int gameFieldFromRow = Math.max(row + rowIndex, 0);
                    int gameFieldToRow = Math.min(gameFieldFromRow + 1, gameField.getRows());

                    // all columns of gameField going before the figure
                    int gameFieldFromColumn = 0;
                    int gameFieldToColumn = Math.min(column + figureEmptyTiles, gameField.getColumns());

                    if (gameFieldFromRow < gameFieldToRow && gameFieldFromColumn < gameFieldToColumn) {

                        int gameFieldEmptyTiles = getCountOfEmptyTiles(
                                gameField,
                                gameFieldFromRow,
                                gameFieldToRow,
                                gameFieldFromColumn,
                                gameFieldToColumn,
                                false);


                        countOfEmptyTiles = Math.min(countOfEmptyTiles, gameFieldEmptyTiles);

                    }

                }

                minColumn = Math.max(column - countOfEmptyTiles, 0);

            }

        }

        if ((border & RIGHT) == RIGHT) {

            if (rows == 0) {

                maxColumn = gameField.getColumns() - columns;

            } else {

                int countOfEmptyTiles = gameField.getColumns() - 1;
                for (int rowIndex = 0; rowIndex < rows; rowIndex++) {

                    // only rows rowIndex
                    int figureFromRow = rowIndex;
                    int figureToRow = rowIndex + 1;

                    // all columns of figure
                    int figureFromColumn = 0;
                    int figureToColumn = columns;

                    int figureEmptyTiles = getCountOfEmptyTiles(
                            figure,
                            figureFromRow,
                            figureToRow,
                            figureFromColumn,
                            figureToColumn,
                            false);


                    // only rows rowIndex
                    int gameFieldFromRow = Math.max(row + rowIndex, 0);
                    int gameFieldToRow = Math.min(gameFieldFromRow + 1, gameField.getRows());

                    // all columns of gameField going after the figure
                    int gameFieldFromColumn = Math.max(column + columns - figureEmptyTiles, 0);
                    int gameFieldToColumn = gameField.getColumns();

                    if (gameFieldFromRow < gameFieldToRow && gameFieldFromColumn < gameFieldToColumn) {

                        int gameFieldEmptyTiles = getCountOfEmptyTiles(
                                gameField,
                                gameFieldFromRow,
                                gameFieldToRow,
                                gameFieldFromColumn,
                                gameFieldToColumn,
                                true);


                        countOfEmptyTiles = Math.min(countOfEmptyTiles, gameFieldEmptyTiles);

                    }
                }

                maxColumn = Math.min(column + countOfEmptyTiles, gameField.getColumns() - columns);

            }

        }

        if ((border & TOP) == TOP) {

            if (columns == 0) {

                minRow = 0;

            } else {

                int countOfEmptyTiles = gameField.getColumns() - 1;
                for (int columnIndex = 0; columnIndex < columns; columnIndex++) {

                    // all rows of figure
                    int figureFromRow = 0;
                    int figureToRow = rows;

                    // only columns columnIndex
                    int figureFromColumn = columnIndex;
                    int figureToColumn = columnIndex + 1;

                    int figureEmptyTiles = getCountOfEmptyTiles(
                            figure,
                            figureFromRow,
                            figureToRow,
                            figureFromColumn,
                            figureToColumn,
                            true);


                    // all rows of game field going before the figure
                    int gameFieldFromRow = 0;
                    int gameFieldToRow = Math.min(row + figureEmptyTiles, gameField.getRows());

                    // only columns columnIndex
                    int gameFieldFromColumn = Math.max(column + columnIndex, 0);
                    int gameFieldToColumn = Math.min(column + columnIndex + 1, gameField.getColumns());

                    if (gameFieldFromRow < gameFieldToRow && gameFieldFromColumn < gameFieldToColumn) {

                        int gameFieldEmptyTiles = getCountOfEmptyTiles(
                                gameField,
                                gameFieldFromRow,
                                gameFieldToRow,
                                gameFieldFromColumn,
                                gameFieldToColumn,
                                false);


                        countOfEmptyTiles = Math.min(countOfEmptyTiles, gameFieldEmptyTiles);

                    }
                }

                minRow = Math.max(row - countOfEmptyTiles, 0);

            }

        }

        if ((border & BOTTOM) == BOTTOM) {

            if (columns == 0) {

                maxRow = gameField.getRows() - rows;

            } else {

                int countOfEmptyTiles = gameField.getRows() - 1;
                for (int columnIndex = 0; columnIndex < columns; columnIndex++) {

                    // all rows of figure
                    int figureFromRow = 0;
                    int figureToRow = rows;

                    // only columns columnIndex
                    int figureFromColumn = columnIndex;
                    int figureToColumn = columnIndex + 1;

                    int figureEmptyTiles = getCountOfEmptyTiles(
                            figure,
                            figureFromRow,
                            figureToRow,
                            figureFromColumn,
                            figureToColumn,
                            false);


                    // all rows of game field going after the figure
                    int gameFieldFromRow = Math.max(row + rows - figureEmptyTiles, 0);
                    int gameFieldToRow = gameField.getRows();

                    // only columns columnIndex
                    int gameFieldFromColumn = Math.max(column + columnIndex, 0);
                    int gameFieldToColumn = Math.min(column + columnIndex + 1, gameField.getColumns());

                    if (gameFieldFromRow < gameFieldToRow && gameFieldFromColumn < gameFieldToColumn) {

                        int gameFieldEmptyTiles = getCountOfEmptyTiles(
                                gameField,
                                gameFieldFromRow,
                                gameFieldToRow,
                                gameFieldFromColumn,
                                gameFieldToColumn,
                                true);

                        countOfEmptyTiles = Math.min(countOfEmptyTiles, gameFieldEmptyTiles);

                    }

                }

                maxRow = Math.min(row + countOfEmptyTiles, gameField.getRows() - rows);

            }

        }

    }

    private int getCountOfEmptyTiles(SquareBlock block, int fromRow, int toRow, int fromColumn, int toColumn, boolean breakIfNotEmpty) {

        int countOfEmptyTiles = 0;

        exit:
        for (int row = fromRow; row < toRow; row++) {
            for (int column = fromColumn; column < toColumn; column++) {

                if (!block.isDataCell(row, column)) {
                    countOfEmptyTiles++;
                    continue;
                }

                if (breakIfNotEmpty) {
                    break exit;
                }

                countOfEmptyTiles = 0;

            }
        }

        return countOfEmptyTiles;

    }


}

