package org.academiadecodigo.networkgame.Server;

/**
 * Created by codecadet on 20/06/16.
 */
public class Position {

    private int col;
    private int row;
    private int width;
    private int height;

    /**
     * set a number of colums and rows
     * and the width and height for collision area
     *
     * @param col number of columns
     * @param row number of rows
     */
    public Position(int col, int row) {
        this.col = col;
        this.row = row;
        width = 3;
        height = 3;
    }

    //GETTERS AND SETTERS

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOutCol() {
        return col - 1;
    }

    public int getOutRow() {
        return row - 1;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
