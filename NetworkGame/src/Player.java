/**
 * Created by codecadet on 20/06/16.
 */
public class Player implements Runnable{

    private Position pos;

    public  Player(){
        pos = new Position(20,20);
    }

    public Position getPos() {
        return pos;
    }

    public void run() {
        //switch
    }
}

