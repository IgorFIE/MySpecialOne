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
                getPos().setRow(getPos().getRow() - getSpeed());
                break;

            case "down":
                getPos().setRow(getPos().getRow()+getSpeed());
                break;

            case "left":
                getPos().setCol(getPos().getCol() - getSpeed());
                break;

            case "right":
                getPos().setCol(getPos().getCol() + getSpeed());
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
