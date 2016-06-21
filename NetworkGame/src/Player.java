import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class Player extends GenericPlayer implements Runnable{

    private Socket clientSocket;
    private PrintWriter out;

    Player(Socket clientSocket){
        super(new Position(20,20));
        this.clientSocket = clientSocket;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {

        System.out.println("running");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {

            out = new PrintWriter(clientSocket.getOutputStream());

            String next;
            while (!isDead() && (next = input.readLine()) != null) {

                switch (next) {

                    case "up":
                        out.println(getPos().getCol() + ":" + (getPos().getRow()-getSpeed()));
                        break;

                    case "down":
                        out.println(getPos().getCol() + ":" + (getPos().getRow()+getSpeed()));
                        break;

                    case "left":
                        out.println((getPos().getCol()-getSpeed()) + ":" + getPos().getRow());
                        break;

                    case "right":
                        out.println((getPos().getCol()+getSpeed()) + ":" + getPos().getRow());
                        break;
                }
            }

            if (isDead()) {
                out.println("dead");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}

