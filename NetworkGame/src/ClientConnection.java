import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class ClientConnection implements Runnable{

    private Socket clientSocket;

    private BufferedReader in = null;
    private PrintWriter out = null;

    private Server myServer;
    private String myName;

    private boolean go;

    /**
     * Constructor
     * @param clientSocket - Socket for player connection to the server.
     * @param server - The server that controls and distributes player updates.
     */
    public ClientConnection(Socket clientSocket, Server server) throws IOException {

        this.clientSocket = clientSocket; this.myServer = server;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        send("Enter your name.");
        myName = in.readLine();
    }

    public void run() {

        String inputStream = "";
        try {
            while ((inputStream = in.readLine())!= null) {

                System.out.println(inputStream);
                myServer.sendToAll(myName + ":" + inputStream);

                if (inputStream.equals("dead")) {
                    myServer.removeFromServer(this);
                    Thread.currentThread().interrupt();
                    this.clientSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error"+e.getMessage());
        }
    }

    //Send command to Game
    public void send(String command){
        System.out.println("sending");
        out.write(command + "\n");
        out.flush();
    }

    //SETTER - Set custom name for player (If needed)
    public void setName(String newName){
        myName = newName;
    }

    //GETTER - Get client name
    public String getMyName() {
        return myName;
    }

    public synchronized void setGo() {
        this.go = true;
        System.out.println("go");
        notifyAll();
    }

    private synchronized void waitForGo() {
        while (!go) {
            System.out.println("waiting");
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


