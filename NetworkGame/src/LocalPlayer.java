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
    private String name;

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
            name = s;
            out.write(name + "\n"); //":" + getHealth() +
            out.flush();

            String next;
            while (!isDead() && (next = input.readLine()) != null) {

                System.out.println(next);
                out.write(next+'\n');
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.write("dead");
            out.flush();
            out.close();
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loseHealth(int dmg) {
        super.loseHealth(dmg);
        System.out.println("dealt damage");
        if(isDead()) {

            System.out.println("deeeead");
            out.write("dead");
            out.flush();
        }
    }

    public String getName() {
        return name;
    }
}

