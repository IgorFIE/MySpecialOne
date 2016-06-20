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

                String inputCommand = in.readLine();

                // TODO - SOUT for debugging purposes only, DELETE this after.
                System.out.println(inputCommand);

                //TODO - Implement proper broadcasting method from Server.
                //myServer.sendAll(inputStream);
            }

        } catch (IOException e) {
            System.out.println("Error with Input/Output Steams"+e.getMessage());
        }

    }


    public void send(String message){
        out.write(message + "\n");
        out.flush();
    }

    //SETTER - For custom client name (if needed)
    public void setName(String newName){
        myName = newName;
    }

}


