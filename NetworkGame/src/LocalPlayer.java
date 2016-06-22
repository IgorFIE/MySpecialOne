import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class LocalPlayer extends Player implements Runnable{

    private Socket clientSocket;
    private PrintWriter out;

    LocalPlayer(Socket clientSocket){
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

            String s = input.readLine();
            System.out.println(s);
            out.write(s + "\n");
            out.flush();

            String next;
            while (!isDead() && (next = input.readLine()) != null) {

                System.out.println(next);
                out.write(next+'\n');
                out.flush();
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

