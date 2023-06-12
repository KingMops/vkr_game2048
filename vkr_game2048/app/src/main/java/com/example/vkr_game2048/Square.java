package com.example.vkr_game2048;

public class Square {
    private int column;
    private int row;
    public int value;
    private boolean canMove;



    public Square(int row, int column, int value) {
        super();
        this.column = column;
        this.row = row;
        this.value = value;
        this.canMove = true;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

}
