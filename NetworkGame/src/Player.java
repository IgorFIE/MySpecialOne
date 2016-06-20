import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class Player implements Runnable{

    private Position pos;
    private Socket clientSocket;
    private PrintWriter out;
    private int speed = 2;
    private int health;

    public  Player(){
        pos = new Position(20,20);
        //this.clientSocket = clientSocket;
        Thread t = new Thread(this);
        t.start();
    }

    public Position getPos() {
        return pos;
    }

    public void run() {

        System.out.println("running");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {

            //out = new PrintWriter(clientSocket.getOutputStream());

            String next;
            while ((next = input.readLine()) != null) {

                switch (next) {

                    case "up":
                        out.println(getPos().getCol() + ":" + (getPos().getRow()-speed));
                        break;

                    case "down":
                        out.println(getPos().getCol() + ":" + (getPos().getRow()+speed));
                        break;

                    case "left":
                        out.println((getPos().getCol()-speed) + ":" + getPos().getRow());
                        break;

                    case "right":
                        out.println((getPos().getCol()+speed) + ":" + getPos().getRow());
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

