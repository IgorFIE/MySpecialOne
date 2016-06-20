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

    /**
     * Constructor
     * @param clientSocket - Socket for player connection to the server.
     * @param server - The server that controls and distributes player updates.
     */
    public ClientConnection(Socket clientSocket, Server server){ this.clientSocket = clientSocket; this.myServer = server;}

    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true){

                String inputStream = in.readLine();

                //TODO - Check proper method name
                myServer.sendToAll(inputStream);

                if(inputStream!=null){
                    //TODO - Check proper method name
                    myServer.removeFromServer(this);
                }
            }

        } catch (IOException e) {
            System.out.println("Error"+e.getMessage());
        }

    }

    //Send command to Game
    public void send(String command){
        out.write(command + "\n");
        out.flush();
    }

    //SETTER - Set custom name for player (If needed)
    public void setName(String newName){
        myName = newName;
    }

}


