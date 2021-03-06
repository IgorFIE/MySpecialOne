/**
 * Created by codecadet on 20/06/16.
 */
public class Position {

    private int col;
    private int row;
    private int width;
    private int height;

    public Position(int col, int row){
        this.col = col;
        this.row = row;
        width = 3;
        height = 3;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOutCol(){
        return col - 1;
    }

    public int getOutRow() {
        return row - 1;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
