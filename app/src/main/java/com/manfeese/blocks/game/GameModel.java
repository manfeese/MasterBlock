package com.manfeese.blocks.game;

import com.manfeese.blocks.game.data.GameField;
import com.manfeese.blocks.game.data.SquareBlock;
import com.manfeese.blocks.game.data.figures.EmptyFigure;
import com.manfeese.blocks.game.data.figures.Figure;
import com.manfeese.blocks.game.data.figures.FigureFactory;

public class GameModel {

    private FigureFactory figureFactory;

    private GameField gameField;
    private Figure nextFigure;
    private Figure figure;
    private Figure emptyFigure;

    private GameState gameState;


    public GameModel() {

        figureFactory = FigureFactory.getInstance();
        gameField = new GameField();
        gameState = new GameState();

    }

    public Figure getFigure() {
        return figure;
    }

    public Figure getEmptyFigure() {
        return emptyFigure;
    }

    public GameField getGameField() {
        return gameField;
    }

    public Figure getNextFigure() {
        return nextFigure;
    }

    public GameState getGameState() {
        return gameState;
    }


    public void start(SquareBlock gameField) {

        gameState.clear();

        initGameField(
                gameField.getRows(),
                gameField.getColumns(),
                gameField.getBackgroundColor());

        setFigure(emptyFigure);
        setNextFigure();

    }

    public void nextMove() {

        autoNextMove();

    }

    public void move(int rows, int columns) {

        if (figure == emptyFigure) {
            return;
        }

        int rowsToMove = figure.getRowsToMove(rows);
        int columnsToMove = figure.getColumnsToMove(columns);

        if (figure.canMove(rowsToMove, columnsToMove)) {
            figure.move(rowsToMove, columnsToMove);
            gameState.addState(GameState.FIGURE_MOOVED);
        }

    }

    public void rotate() {

        if (figure == emptyFigure) {
            return;
        }

        if (figure.rotate()) {
            gameState.addState(GameState.FIGURE_ROTATED);
        }

    }



    private void initGameField(int rows, int columns, int backgroundColor) {

        gameField.setBackgroundColor(backgroundColor);
        gameField.setRows(rows);
        gameField.setColumns(columns);
        gameField.init();

        emptyFigure = new EmptyFigure(gameField);
        emptyFigure.init();

        gameState.addState(GameState.GAME_FIELD_CHANGED);

    }

    private void autoNextMove() {

        // 1. add a new figure
        if (figure == emptyFigure) {

            setFigure(nextFigure);
            setNextFigure();

            if (figure.getPosition().isOverlay()) {
                gameState.addState(GameState.GAME_OVER);
            }

            return;

        }

        // 2. move figure down at one block
        if (figure.canMove(1, 0)) {
            figure.move(1, 0);
            gameState.addState(GameState.FIGURE_MOOVED);
            return;
        }

        // 3. update gamefield
        copyFigureToGameField();
        removeFullLines();

        figure = emptyFigure;
        autoNextMove();

    }


    private void setFigure(Figure newFigure) {

        figure = newFigure;

        int row = 0;
        int column = (gameField.getColumns() - figure.getColumns()) / 2;
        figure.setPosition(row, column);

        gameState.addState(GameState.FIGURE_CHANGED);

    }

    private void setNextFigure() {

        nextFigure = figureFactory.getRandomFigure(gameField);

        gameState.addState(GameState.NEXT_FIGURE_CHANGED);

    }

    private void copyFigureToGameField() {

        gameField.copyFigureToGameField(figure);

        // set to zero score multiplier
        if (gameField.countOfFullLines() == 0) {
            gameState.scoreMultiplier = 0;
        }

        gameState.addState(GameState.GAME_FIELD_CHANGED);

    }

    private void removeFullLines() {

        int[] removedLines = gameField.removeFullLines();
        if (removedLines.length > 0) {

            gameState.setRemovedLines(removedLines);

            for (int i = 1; i <= removedLines.length; i++) {
                gameState.scoreMultiplier += i;
            }

            gameState.score += gameField.getColumns() * gameState.scoreMultiplier;
            if (gameState.score > gameState.highScore) {
                gameState.highScore = gameState.score;
            }

            gameState.addState(GameState.SCORE_CHANGED);

        }

    }


    public static class GameState {

        public static final int NO_ACTION = 0;
        public static final int GAME_OVER = 0x01;
        public static final int FIGURE_CHANGED = 0x02;
        public static final int FIGURE_MOOVED = 0x04;
        public static final int FIGURE_ROTATED = 0x08;
        public static final int NEXT_FIGURE_CHANGED = 0x10;
        public static final int GAME_FIELD_CHANGED = 0x20;
        public static final int ROWS_REMOVED = 0x40;
        public static final int SCORE_CHANGED = 0x80;

        private int state;

        private int scoreMultiplier;
        private int score;
        private int highScore;

        private int[] removedLines;


        public GameState() {
            state = NO_ACTION;
        }

        public void clear() {

            state = NO_ACTION;

            scoreMultiplier = 0;
            score = 0;

            removedLines = null;
            addState(SCORE_CHANGED);

        }

        public boolean hasState(int state) {
            return (this.state & state) == state;
        }

        public void addState(int state) {
            this.state |= state;
        }

        public void removeState(int state) {
            this.state ^= state;
        }


        public int getScore() {
            return score;
        }

        public int getHighScore() {
            return highScore;
        }

        public void setHighScore(int highScore) {
            this.highScore = highScore;
            addState(SCORE_CHANGED);
        }


        public int[] getRemovedLines() {
            return removedLines;
        }

        public void setRemovedLines(int[] removedLines) {
            this.removedLines = removedLines;
            addState(GAME_FIELD_CHANGED);
        }
    }


}
