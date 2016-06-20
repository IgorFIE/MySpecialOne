/**
 * Created by codecadet on 20/06/16.
 */
public class Position {

    private int col;
    private int row;

    public Position(int col, int row){
        this.col = col;
        this.row = row;
    }

    public void setRow(int row) {
        this.row += row;
    }

    public void setCol(int col) {
        this.col += col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
