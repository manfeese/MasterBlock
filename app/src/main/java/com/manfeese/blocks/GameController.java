package com.manfeese.blocks;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.manfeese.blocks.game.GameModel;
import com.manfeese.blocks.game.data.figures.Figure;
import com.manfeese.blocks.game.data.Position;
import com.manfeese.blocks.game.data.SquareBlock;
import com.manfeese.blocks.view.touch.TouchListener;

import java.util.Timer;
import java.util.TimerTask;

public class GameController implements TouchListener {

    private MainActivity activity;
    private GameModel gameModel;
    private GameCounter gameCounter;

    private boolean gameOver;
    private boolean gameStarted;
    private boolean gamePaused;


    public GameController() {
        gameModel = new GameModel();
        gameCounter = new GameCounter() {

            @Override
            public void doInBackground() {
                gameModel.nextMove();
            }

            @Override
            public void onPostExecute() {
                onGameStateChanged(false);
            }
        };
    }


    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(boolean gamePaused) {

        if (this.gamePaused == gamePaused) return;

        this.gamePaused = gamePaused;
        if (gamePaused) {
            gameCounter.end();
        } else {
            gameCounter.start(500);
        }
    }

    public boolean isGameRunning() {
        return gameStarted && !gamePaused && !gameOver;
    }


    @Override
    public void onMove(int rows, int columns, boolean isInteractive) {

        if (!isGameRunning()) {
            return;
        }

        // can't move up
        if (rows < 0) {
            return;
        }

        gameModel.move(rows, columns);
        onGameStateChanged(isInteractive);


    }

    @Override
    public void onRotate() {

        if (!isGameRunning()) {
            return;
        }

        gameModel.rotate();
        onGameStateChanged(true);

    }


    public void start(SquareBlock initGameField) {

        gameStarted = true;
        gamePaused = false;
        gameOver = false;

        gameModel.start(initGameField);
        gameCounter.setElapsedTime(0);
        gameCounter.start(0);

    }

    public void onSaveInstanceState(SharedPreferences.Editor settings) {

        GameModel.GameState gameState = gameModel.getGameState();

        settings.putInt("highScore", gameState.getHighScore());

    }

    public void onRestoreInstanceState(SharedPreferences settings) {

        int highScore = settings.getInt("highScore", 0);

        GameModel.GameState gameState = gameModel.getGameState();
        gameState.setHighScore(highScore);

        onGameStateChanged(false);

    }

    private void onGameStateChanged(boolean isInteractive) {

        GameModel.GameState gameState = gameModel.getGameState();

        if (gameState.hasState(GameModel.GameState.GAME_OVER)) {
            gameOver = true;
            gameCounter.end();
            activity.onGameOver();
            gameState.removeState(GameModel.GameState.GAME_OVER);
        }

        if (gameState.hasState(GameModel.GameState.NEXT_FIGURE_CHANGED)) {
            activity.onNextFigureChanged(gameModel.getNextFigure());
            gameState.removeState(GameModel.GameState.NEXT_FIGURE_CHANGED);
        }

        if (gameState.hasState(GameModel.GameState.GAME_FIELD_CHANGED)) {

            activity.onGameFieldChanged(gameModel.getGameField());

            Figure figure = gameModel.getEmptyFigure();
            Position position = figure.getPosition();
            activity.onFigureChanged(figure, position);

            gameState.removeState(GameModel.GameState.GAME_FIELD_CHANGED);
        }

        if (gameState.hasState(GameModel.GameState.ROWS_REMOVED)) {
            activity.onRowsRemoved(gameModel.getGameField());
            gameState.removeState(GameModel.GameState.ROWS_REMOVED);
        }

        if (gameState.hasState(GameModel.GameState.SCORE_CHANGED)) {
            activity.onScoreChanged(gameState.getScore(), gameState.getHighScore());
            gameState.removeState(GameModel.GameState.SCORE_CHANGED);
        }

        if (gameState.hasState(GameModel.GameState.FIGURE_CHANGED)) {

            Figure figure = gameModel.getFigure();
            Position position = figure.getPosition();
            activity.onFigureChanged(figure, position);

            gameState.removeState(GameModel.GameState.FIGURE_CHANGED);
        }

        if (gameState.hasState(GameModel.GameState.FIGURE_MOOVED)) {

            Position newPosition = gameModel.getFigure().getPosition();
            activity.onMove(newPosition, isInteractive);

            gameState.removeState(GameModel.GameState.FIGURE_MOOVED);

        }

        if (gameState.hasState(GameModel.GameState.FIGURE_ROTATED)) {

            Figure figure = gameModel.getFigure();
            Position position = figure.getPosition();
            activity.onRotate(figure, figure.getRotationDegree(), position);

            gameState.removeState(GameModel.GameState.FIGURE_ROTATED);

        }

    }




}

abstract class GameCounter {

    private static final String LOG_TAG = "GameCounter";
    private static final boolean DEBUG = false;

    private static final int SPEED_MIN = 800;
    private static final int SPEED_MAX = 150;
    private static final int SPEED_CHANGE = 50;
    private static final int SPEED_CHANGE_RATE = 60 * 1000;

    private Handler handler;
    private Timer timer;
    private TimerTask mainTask;
    private Runnable onPostRunnable;

    private long elapsedTime;
    private int speed;
    private boolean isRunning;


    GameCounter() {
        this(0);
    }

    GameCounter(long elapsedTime) {

        this.elapsedTime = elapsedTime;
        this.handler = new Handler();
        this.timer = new Timer(true);

        isRunning = false;
        speed = SPEED_MIN;

        onPostRunnable = new Runnable() {
            @Override
            public void run() {
                if (DEBUG) {
                    Log.i(LOG_TAG, "onPostRunnable : startGame");
                }

                onPostExecute();

                if (DEBUG) {
                    Log.i(LOG_TAG, "onPostRunnable : end");
                }

            }
        };

    }


    public long getElapsedTime() {
        return elapsedTime;
    }

    void setElapsedTime(long elapsedTime) {

        this.elapsedTime = elapsedTime;
        setupSpeed();

    }


    void start(int delay) {

        if (DEBUG) {
            Log.i(LOG_TAG, "startGame : startGame");
        }

        if (isRunning) {
            end();
        }
        startTimer(delay);

        if (DEBUG) {
            Log.i(LOG_TAG, "startGame : end");
        }

    }

    void end() {

        if (DEBUG) {
            Log.i(LOG_TAG, "end : startGame");
        }

        if (isRunning) {

            mainTask.cancel();
            isRunning = false;

        }

        if (DEBUG) {
            Log.i(LOG_TAG, "end : end");
        }

    }


    abstract void doInBackground();

    abstract void onPostExecute();


    void startTimer(int delay) {

        mainTask = new TimerTask() {
            @Override
            public void run() {

                if (DEBUG) {
                    Log.i(LOG_TAG, "mainTask : startGame");
                }

                doInBackground();
                handler.post(onPostRunnable);

                elapsedTime += speed;
                setupSpeed();

                if (DEBUG) {
                    Log.i(LOG_TAG, "mainTask : end");
                }


            }
        };


        timer.schedule(mainTask, delay, speed);
        isRunning = true;

    }

    void setupSpeed() {

        if (DEBUG) {
            Log.i(LOG_TAG, "setupSpeed : startGame");
        }

        int level = (int) (elapsedTime / SPEED_CHANGE_RATE);
        int newSpeed = Math.max(SPEED_MIN - level * SPEED_CHANGE, SPEED_MAX);

        if (speed != newSpeed) {

            speed = newSpeed;

            if (isRunning) {
                end();
                startTimer(speed);
            }

        }

        if (DEBUG) {
            Log.i(LOG_TAG, "setupSpeed : end");
        }


    }

}
