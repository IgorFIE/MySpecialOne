import java.io.IOException;
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

    /**
     * wait for two client connections then start a new game
     *
     * When a player connects add him to the client list and submit it
     * to the thread pool
     */
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

        //TODO what we will send???
        Game game = new Game();
        game.start();
    }

    /**
     * send a respective String to all clients in the client List
     *
     * @param out String to send
     */
    public synchronized void sendToAll(String out){
        for (ClientConnection c: clientList) {
            c.send(out);
        }
    }

    /**
     * Remove a client from the Client List
     *
     * @param client client to remove from the client list
     */
    public void removeFromServer(ClientConnection client){
        clientList.remove(client);
    }
}
