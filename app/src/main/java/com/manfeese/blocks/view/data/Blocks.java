package com.manfeese.blocks.view.data;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class Blocks {


    private int[][] blockField;
    private StyleParams styleParams;

    public Blocks() {
        this.styleParams = new StyleParams();
    }

    public StyleParams getStyleParams() {
        return styleParams;
    }

    public void setStyleParams(StyleParams styleParams) {
        this.styleParams = styleParams;
    }


    public void init() {

        blockField = new int[styleParams.rows][styleParams.columns];
        fillBlockField(styleParams.color);

    }

    private void fillBlockField(@ColorInt int value) {
        for (int rowIndex = 0; rowIndex < styleParams.rows; rowIndex++) {
            fillRow(rowIndex, value);
        }
    }

    private void fillRow(int rowIndex, @ColorInt int value) {
        for (int columnIndex = 0; columnIndex < styleParams.columns; columnIndex++) {
            setCellValue(rowIndex, columnIndex, value);
        }
    }

    private void setCellValue(int rowIndex, int columnIndex, @ColorInt int value) {
        if (value == Color.TRANSPARENT) {
            value = styleParams.color;
        }
        blockField[rowIndex][columnIndex] = value;
    }

    public class StyleParams {

        private int rows;
        private int columns;

        private float cellSize;
        private float margin;

        @ColorInt
        private int color;


        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public int getColumns() {
            return columns;
        }

        public void setColumns(int columns) {
            this.columns = columns;
        }

        public float getCellSize() {
            return cellSize;
        }

        public void setCellSize(float cellSize) {
            this.cellSize = cellSize;
        }

        public float getMargin() {
            return margin;
        }

        public void setMargin(float margin) {
            this.margin = margin;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }


}
