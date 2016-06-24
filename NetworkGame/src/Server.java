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
    private boolean letsGo;

    /**
     * Creates a new server with a specific port them calls the Method init
     * <p>
     * If init Stops close the socket if it´s not null
     */
    public Server() {

        try {
            serverSocket = new ServerSocket(portNumber);
            init();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeServer();
        }
    }

    /**
     * waits for clientConnections
     * then waits for all their names
     * then set go to all the players
     * then send to all the clients all their names and start
     * then it will wait until the room isn´t full
     */
    public void init() {
        try {

            while (true) {
                waitClientConnections();

                sendToAll(clientList.get(0).getMyName() + ":" + clientList.get(1).getMyName());
                sendToAll("start");
                roomFull();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the server Socket
     */
    public void closeServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will wait for two players to connect
     * For each it will create a new ClientConnection Thread
     * and add it to the clientList and poll
     *
     * @throws IOException
     */
    private void waitClientConnections() throws IOException {
        while (clientList.size() < 2) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("###" + clientSocket.getLocalAddress() + ": has enter the room! ###");
            ClientConnection client = new ClientConnection(clientSocket, this);
            pool.submit(client);
            clientList.add(client);
            client.send("" + clientList.size());
        }
        System.out.println("room full");
    }

    /**
     * while room full wait
     */
    private synchronized void roomFull() {
        while (clientList.size() == 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * send a respective String to all clients in the client List
     *
     * @param out String to send
     */
    public synchronized void sendToAll(String out) {
        for (ClientConnection c : clientList) {
            c.send(out);
        }
        notifyAll();
    }

    /**
     * Remove a client from the Client List
     *
     * @param client client to remove from the client list
     */
    public synchronized void removeFromServer(ClientConnection client) {
        clientList.remove(client);
        notifyAll();
    }
}
