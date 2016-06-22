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
     * waits for clientConnections
     * then waits for all their names
     * then set go to all the players
     * then send to all the clients all their names and start
     * then it will wait until the room isn´t full
     */
    public synchronized void init() {
        try {
            while (true) {

                waitClientConnections();
                waitClientNames();
                setGoToAll();

                sendToAll(clientList.get(0).getMyName() + ":" + clientList.get(1).getMyName());
                sendToAll("start");

                roomFull();
            }

        } catch (IOException | InterruptedException e) {
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
    private synchronized void waitClientConnections() throws IOException {
        while (clientList.size() < 2) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("###" + clientSocket.getLocalAddress() + ": has enter the room! ###");
            ClientConnection client = new ClientConnection(clientSocket, this);
            clientList.add(client);
            pool.submit(client);
        }
        System.out.println("room full, waiting for names");
    }

    /**
     * This method will wait for all the clients Names
     *
     * @throws InterruptedException
     */
    private synchronized void waitClientNames() throws InterruptedException {
        while (!letsGo) {
            System.out.println("server still waiting for all names set");
            wait(2000);
        }
    }

    /**
     * Set Go to all clientConnections
     */
    private synchronized void setGoToAll() {
        for (ClientConnection c : clientList) {
            c.setGo();
        }
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
     * check if all players have set their names
     * If true we change the value of letsGo to True
     */
    public void areYouReady() {
        int i;
        for (i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).getMyName() == null) break;
        }

        if (i == 2) letsGo = true;
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
    public void removeFromServer(ClientConnection client) {
        clientList.remove(client);
    }
}
