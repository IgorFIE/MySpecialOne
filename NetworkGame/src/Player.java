/**
 * Created by codecadet on 21/06/16.
 */
public class Player {

    private Position pos;
    private int speed = 2;
    private int health;
    private int strength;
    private boolean dead;
    private boolean hasAttacked;

    public Player(Position pos) {
        this.pos = pos;
        health = 5;
        strength = 2;
    }

    public int attack() {

        if (!hasAttacked) {
            hasAttacked = true;
            return strength;
        }
        return 0;
    }

    public void loseHealth(int dmg) {
        System.out.println("lost health");
        health -= dmg;
        System.out.println("current health" + health);
        if (health <= 0) {
            health = 0;
            dead = true;
        }
    }

//    public void move(String direction) {
//
//        hasAttacked = false;
//
//        switch (direction) {
//
//            case "up":
//                if (getPos().getRow() - getSpeed() < 0) {
//                    getPos().setRow(0);
//                } else {
//                    getPos().setRow(getPos().getRow() - getSpeed());
//                }
//                break;
//
//            case "down":
//                if (getPos().getRow() + getSpeed() > Field.height - 1) {
//                    getPos().setRow(Field.height - 1);
//                } else {
//                    getPos().setRow(getPos().getRow() + getSpeed());
//                }
//                break;
//
//            case "left":
//                if (getPos().getCol() - getSpeed() < 0) {
//                    getPos().setCol(0);
//                } else {
//                    getPos().setCol(getPos().getCol() - getSpeed());
//                }
//                break;
//
//            case "right":
//                if (getPos().getCol() + getSpeed() > Field.width - 1) {
//                    getPos().setCol(Field.width - 1);
//                } else {
//                    getPos().setCol(getPos().getCol() + getSpeed());
//                }
//                break;
//
//        }
//    }

    public boolean isDead() {
        return dead;
    }

    public int getSpeed() {
        return speed;
    }

    public Position getPos() {
        return pos;
    }

    public boolean hasAttacked() {

        return hasAttacked;
    }

    public int getHealth() {
        return health;
    }
}
