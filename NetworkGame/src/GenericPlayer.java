/**
 * Created by codecadet on 21/06/16.
 */
public class GenericPlayer {

    private Position pos;
    private int speed = 2;
    private int health;
    private boolean dead;

    public GenericPlayer(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public void loseHealth(int health) {
        this.health -= health;
        dead = health > 0;
    }

    public boolean isDead() {
        return dead;
    }

    public int getSpeed() {
        return speed;
    }
}
