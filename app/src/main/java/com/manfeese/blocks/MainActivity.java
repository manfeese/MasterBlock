package com.manfeese.blocks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.manfeese.blocks.animation.AnimationManager;
import com.manfeese.blocks.game.data.SquareBlock;
import com.manfeese.blocks.game.data.figures.FigureFactory;
import com.manfeese.blocks.game.data.Position;
import com.manfeese.blocks.view.BlocksView;
import com.manfeese.blocks.view.utils.CoordinateUtils;
import com.manfeese.squareBlocks.R;

public class MainActivity extends Activity {

    private static final String APP_PREFERENCES = "mysettings";

    private GameController gameController;
    private AnimationManager animationManager;

    private BlocksView gameFieldView;
    private BlocksView figureView;
    private BlocksView shadowView;
    private BlocksView nextFigureView;

    private ConstraintLayout menuLayer;
    private TextView gameOverButton;
    private TextView scoreView;
    private TextView highScoreView;

    private SharedPreferences mSettings;

    private AnimatorSet figureAnimatorSet;
    private AnimatorSet menuLayerAnimatorSet;

    private long timeMillisOnExit;
    private Toast toastOnExit;
    private AlertDialog.Builder onGameRestartDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // animatorSet FULLSCREEN mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        gameController.onRestoreInstanceState(mSettings);

    }

    @Override
    protected void onPause() {
        super.onPause();

        gameController.setGamePaused(true);

        SharedPreferences.Editor settings = mSettings.edit();
        gameController.onSaveInstanceState(settings);
        settings.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisibility();
    }

    @Override
    public void onBackPressed() {

        if (gameController.isGameRunning()) {
            gameController.setGamePaused(true);
            setVisibility();
            return;
        }

        if (canExit()) {
            super.onBackPressed();
        }

    }


    public void startGame(View v) {

        if (gameController.isGameStarted() && !gameController.isGameOver()) {
            onGameRestartDialog.show();
        } else {
            startGameImmediately();
        }

    }

    public void gameOverContinueGame(View v) {

        if (!gameController.isGameOver()) {
            showMenuLayer(false);
            gameController.setGamePaused(false);
        }

    }

    public void pauseGame(View v) {

        gameController.setGamePaused(true);
        setVisibility();

    }


    public void onMove(Position position, boolean isInteractive) {

        if (figureAnimatorSet != null && figureAnimatorSet.isRunning()) {
            figureAnimatorSet.end();
        }

        int row = position.getRow();
        int column = position.getColumn();
        int maxRow = position.getMaxRow();

        float nextX = CoordinateUtils.getCoordinate(figureView, column);
        float nextY = CoordinateUtils.getCoordinate(figureView, row);
        float maxY = CoordinateUtils.getCoordinate(shadowView, maxRow);

        if (isInteractive) {

            figureView.setX(nextX);
            figureView.setY(nextY);
            figureView.invalidate();

            shadowView.setX(nextX);
            shadowView.setY(maxY);
            shadowView.invalidate();

        } else {

            // 10 millis per cell
            int velocity = 10;

            figureAnimatorSet = new AnimatorSet();
            figureAnimatorSet.playTogether(
                    ObjectAnimator.ofFloat(figureView, "translationX", nextX),
                    ObjectAnimator.ofFloat(figureView, "translationY", nextY),
                    ObjectAnimator.ofFloat(shadowView, "translationX", nextX),
                    ObjectAnimator.ofFloat(shadowView, "translationY", maxY));
            figureAnimatorSet.setInterpolator(new DecelerateInterpolator());
            figureAnimatorSet.setDuration(velocity * Math.max(row, column));
            figureAnimatorSet.start();
        }

    }

    public void onRotate(final SquareBlock squareBlock, int rotation, final Position position) {

        if (figureAnimatorSet != null && figureAnimatorSet.isRunning()) {
            figureAnimatorSet.end();
        }

        final float currentX = figureView.getX();
        final float currentY = figureView.getY();
        final float maxY = shadowView.getY();

        final float nextX = CoordinateUtils.getCoordinate(figureView, position.getColumn());
        final float nextY = CoordinateUtils.getCoordinate(figureView, position.getRow());
        final float nextMaxY = CoordinateUtils.getCoordinate(shadowView, position.getMaxRow());

        float width = CoordinateUtils.getViewCoordinate(figureView, CoordinateUtils.ViewCoordinate.WIDTH);
        float height = CoordinateUtils.getViewCoordinate(figureView, CoordinateUtils.ViewCoordinate.HEIGHT);

        float centerX = width / 2;
        float centerY = height / 2;

        float delta = (centerX - centerY);

        figureAnimatorSet = new AnimatorSet();
        figureAnimatorSet.playTogether(

                ObjectAnimator.ofFloat(figureView, "rotation", 0, rotation),
                ObjectAnimator.ofFloat(figureView, "translationX", currentX, nextX + delta),
                ObjectAnimator.ofFloat(figureView, "translationY", currentY, nextY - delta),

                ObjectAnimator.ofFloat(shadowView, "rotation", 0, rotation),
                ObjectAnimator.ofFloat(shadowView, "translationX", currentX, nextX + delta),
                ObjectAnimator.ofFloat(shadowView, "translationY", maxY, nextMaxY - delta)

        );
        figureAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                changeFigure(squareBlock, position);
            }
        });
        figureAnimatorSet.setInterpolator(new DecelerateInterpolator());
        figureAnimatorSet.setDuration(100);
        figureAnimatorSet.start();

    }

    public void onFigureChanged(SquareBlock squareBlock, Position position) {

        if (figureAnimatorSet != null && figureAnimatorSet.isRunning()) {
            figureAnimatorSet.end();
        }

        changeFigure(squareBlock, position);

    }

    public void onNextFigureChanged(final SquareBlock squareBlock) {

        nextFigureView.setBlock(squareBlock);

    }

    public void onGameFieldChanged(final SquareBlock squareBlock) {

        gameFieldView.setBlock(squareBlock);

    }

    public void onRowsRemoved(final SquareBlock squareBlock) {

        gameFieldView.setBlock(squareBlock);

    }

    public void onScoreChanged(int score, int highScore) {
        scoreView.setText(getResources().getString(R.string.score) + ": " + score);
        highScoreView.setText(getResources().getString(R.string.high_score) + ": " + highScore);
    }

    public void onGameOver() {
        setVisibility();
    }


    private void init() {

        FigureFactory.getInstance().setColors(
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.red_light),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.green_light),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.blue_light),
                getResources().getColor(R.color.violet)
        );

        gameController = new GameController();
        gameController.setActivity(this);


        gameFieldView = findViewById(R.id.gameField);
        gameFieldView.setOnTouchListener(gameController);

        nextFigureView = findViewById(R.id.nextFigure);
        figureView = findViewById(R.id.figure);
        shadowView = findViewById(R.id.shadow);

        gameOverButton = findViewById(R.id.gameOverButton);
        gameOverButton.setVisibility(View.GONE);

        menuLayer = findViewById(R.id.menu_layer);

        scoreView = findViewById(R.id.scoreView);
        highScoreView = findViewById(R.id.highScoreView);


        onGameRestartDialog = new AlertDialog.Builder(this);
        onGameRestartDialog.setTitle(R.string.on_game_restart_title);
        onGameRestartDialog.setMessage(R.string.on_game_restart_message);
        onGameRestartDialog.setPositiveButton(R.string.on_game_restart_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                startGameImmediately();
            }
        });
        onGameRestartDialog.setNegativeButton(R.string.on_game_restart_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
            }
        });
        onGameRestartDialog.setCancelable(true);

    }

    private void startGameImmediately() {

        gameController.start(gameFieldView.getBlock());
        showMenuLayer(false);

    }


    private void setVisibility() {
        showMenuLayer(true);
        gameOverButton.setVisibility(gameController.isGameStarted() || gameController.isGameOver() ? View.VISIBLE : View.GONE);
        gameOverButton.setText(gameController.isGameOver() ? R.string.game_over : R.string.continue_game);
    }

    private void changeFigure(SquareBlock squareBlock, Position position) {

        changeBlocksView(
                figureView,
                squareBlock,
                position.getRow(),
                position.getColumn());

        changeShadow(squareBlock, position);

    }

    private void changeShadow(SquareBlock squareBlock, Position position) {

        SquareBlock shadow = new SquareBlock();
        shadow.init(squareBlock, getResources().getColor(R.color.colorShadow));

        changeBlocksView(
                shadowView,
                shadow,
                position.getMaxRow(),
                position.getColumn());

    }

    private void changeBlocksView(BlocksView view, SquareBlock block, int row, int column) {

        float coordinateX = CoordinateUtils.getCoordinate(view, column);
        float coordinateY = CoordinateUtils.getCoordinate(view, row);

        view.setRotation(0);
        view.setX(coordinateX);
        view.setY(coordinateY);

        view.setBlock(block);

    }


    private boolean canExit() {

        if (System.currentTimeMillis() - timeMillisOnExit < 2000) {
            toastOnExit.cancel();
            return true;
        }

        toastOnExit = Toast.makeText(this, R.string.on_exit, Toast.LENGTH_SHORT);
        toastOnExit.show();

        timeMillisOnExit = System.currentTimeMillis();

        return false;
    }

    private void showMenuLayer(final boolean show) {

        if (menuLayerAnimatorSet != null && menuLayerAnimatorSet.isRunning()) {
            menuLayerAnimatorSet.end();
        }

        final int visibility = show ? View.VISIBLE : View.INVISIBLE;

        if (menuLayer.getVisibility() == visibility) {
            return;
        }

        float translationX, translationY;
        float scaleX, scaleY;

        if (show) {

            translationX = 0;
            translationY = 0;

            scaleX = 1.0f;
            scaleY = 1.0f;

        } else {

            translationX = menuLayer.getWidth();
            translationY = -menuLayer.getHeight();

            scaleX = 0f;
            scaleY = 0f;

        }

        menuLayerAnimatorSet = new AnimatorSet();
        menuLayerAnimatorSet.playTogether(

                ObjectAnimator.ofFloat(menuLayer, "translationX", translationX),
                ObjectAnimator.ofFloat(menuLayer, "translationY",translationY),

                ObjectAnimator.ofFloat(menuLayer, "scaleX", scaleX),
                ObjectAnimator.ofFloat(menuLayer, "scaleY", scaleY)

        );
        menuLayerAnimatorSet.setInterpolator(new DecelerateInterpolator());
        menuLayerAnimatorSet.setDuration(500);
        menuLayerAnimatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (show) {
                    menuLayer.setVisibility(visibility);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!show) {
                    menuLayer.setVisibility(visibility);
                }

            }
        });
        menuLayerAnimatorSet.start();

    }


}
