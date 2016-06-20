import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 20/06/16.
 */
public class Server {

    private final int portNumber = 8080;
    private ServerSocket serverSocket = null;
    private List<ClientConnection> clientList = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    /**
     * Creates a new server with a specific port them calls the Method init
     *
     * If init Stops close the socket if it´s null
     */
    public Server(){

        try {
            serverSocket = new ServerSocket(portNumber);
            init();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void init(){

        while(clientList.size() < 2){

            try {

                Socket clientSocket = serverSocket.accept();

                System.out.println("###" + clientSocket.getLocalAddress() + ": has enter the room! ###");

                ClientConnection client = new ClientConnection(clientSocket, this);
                clientList.add(client);
                pool.submit(new Thread(client));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Game game = new Game(clientList);
        game.start();

    }

    public synchronized void sendToAll(String out){
        for (ClientConnection c: clientList) {
            c.send(out);
        }
    }

    public void removeFromServer(ClientConnection client){
        clientList.remove(client);
    }
}
