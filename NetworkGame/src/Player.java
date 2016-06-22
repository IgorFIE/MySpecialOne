/**
 * Created by codecadet on 21/06/16.
 */
public class Player {

    private Position pos;
    private int speed = 2;
    private int health;
    private boolean dead;

    public Player(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public void loseHealth(int health) {
        this.health -= health;
        dead = health > 0;
    }

    public void move(String direction) {

        switch (direction) {

            case "up":
                if(getPos().getRow() - getSpeed()<=0){
                    getPos().setRow(0);
                } else {
                    getPos().setRow(getPos().getRow() - getSpeed());
                }
                break;

            case "down":
                if(getPos().getRow() + getSpeed()>=Field.height-1){
                    getPos().setRow(Field.height-1);
                } else {
                    getPos().setRow(getPos().getRow() + getSpeed());
                }
                break;

            case "left":
                if(getPos().getCol() - getSpeed()<=0){
                    getPos().setCol(0);
                } else {
                    getPos().setCol(getPos().getCol() - getSpeed());
                }
                break;

            case "right":
                if(getPos().getCol() + getSpeed()>=Field.width-1){
                    getPos().setCol(Field.width-1);
                } else {
                    getPos().setCol(getPos().getCol() + getSpeed());
                }
                break;

            case "attack":

                break;

        }
    }


    public boolean isDead() {
        return dead;
    }

    public int getSpeed() {
        return speed;
    }
}
