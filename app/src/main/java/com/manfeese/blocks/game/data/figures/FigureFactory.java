package com.manfeese.blocks.game.data.figures;

import android.graphics.Color;

import com.manfeese.blocks.game.data.GameField;

import java.util.Random;

public class FigureFactory {

    private static FigureFactory instance = new FigureFactory();

    public static FigureFactory getInstance() {
        return instance;
    }


    private Random generator;
    private String[] figures;
    private int[] colors;


    private FigureFactory(){
        init();
    }

    public void setColors(int... colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }



    public Figure getRandomFigure(GameField gameField) throws RuntimeException {

        if (colors == null) {
            throw new RuntimeException("Colors is not initialized");
        }

        // another figure
        int index = generator.nextInt(figures.length);
        int colorIndex = colors.length == 0 ? -1 : generator.nextInt(colors.length);
        int rotateIndex = generator.nextInt(4);

        int backgroundColor = Color.TRANSPARENT;
        int dataColor = colorIndex >= 0 ? colors[colorIndex] : backgroundColor;

        String figureName = figures[index];
        Figure figure = null;

        switch (figureName) {

            case "L" :
                figure = getFigureL(gameField, dataColor, rotateIndex);
                break;

            case "J" :
                figure = getFigureJ(gameField, dataColor, rotateIndex);
                break;

            case "I" :
                figure = getFigureI(gameField, dataColor, rotateIndex);
                break;

            case "O" :
                figure = getFigureO(gameField, dataColor, rotateIndex);
                break;

            case "S" :
                figure = getFigureS(gameField, dataColor, rotateIndex);
                break;

            case "Z" :
                figure = getFigureZ(gameField, dataColor, rotateIndex);
                break;

            case "T" :
                figure = getFigureT(gameField, dataColor, rotateIndex);
                break;

        }

        return figure;

    }

    private Figure getFigureL(GameField gameField, int dataColor, int rotationState) {
        return new FigureL(gameField, dataColor, rotationState);
    }

    private Figure getFigureJ(GameField gameField, int dataColor, int rotationState) {
        return new FigureJ(gameField, dataColor, rotationState);
    }

    private Figure getFigureI(GameField gameField, int dataColor, int rotationState) {
        return new FigureI(gameField, dataColor, rotationState);
    }

    private Figure getFigureO(GameField gameField, int dataColor, int rotationState) {
        return new FigureO(gameField, dataColor, rotationState);
    }

    private Figure getFigureS(GameField gameField, int dataColor, int rotationState) {
        return new FigureS(gameField, dataColor, rotationState);
    }

    private Figure getFigureZ(GameField gameField, int dataColor, int rotationState) {
        return new FigureZ(gameField, dataColor, rotationState);
    }

    private Figure getFigureT(GameField gameField, int dataColor, int rotationState) {
        return new FigureT(gameField, dataColor, rotationState);
    }


    private void init() {

        generator = new Random(System.nanoTime());
        initFigures();

    }

    private void initFigures() {

        figures = new String[] {
                "L",
                "J",
                "I",
                "O",
                "S",
                "Z",
                "T",
            };

    }


}
