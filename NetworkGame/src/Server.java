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
     *
     * If init Stops close the socket if it´s not null
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
    public synchronized void init(){

        try{

            while(true) {

                while (clientList.size() < 2) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("###" + clientSocket.getLocalAddress() + ": has enter the room! ###");
                    ClientConnection client = new ClientConnection(clientSocket, this);
                    clientList.add(client);
                    pool.submit(client);
                }

                System.out.println("room full, waiting for names");


                while (!letsGo) {
                    System.out.println("server still waiting for all names set");
                    wait(2000);
                }

                System.out.println("about to set go");
                for (ClientConnection c: clientList) {

                    c.setGo();
                    System.out.println("goSet");
                }

                sendToAll(clientList.get(0).getMyName() + ":" + clientList.get(1).getMyName());
                sendToAll("start");

                while (clientList.size() == 2) {

                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
        e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * send a respective String to all clients in the client List
     *
     * @param out String to send
     */
    public synchronized void sendToAll(String out){
        for (ClientConnection c: clientList) {
            System.out.println("trying to print shit");
            c.send(out);

        }
        notifyAll();
    }

    /**
     * Remove a client from the Client List
     *
     * @param client client to remove from the client list
     */
    public void removeFromServer(ClientConnection client){
        clientList.remove(client);
    }

    public void areYouReady (){
        int i;
        for (i = 0; i < clientList.size(); i++) {

            if (clientList.get(i).getMyName()==null) {
                break;
            }
        }

        if (i == 2) {
            letsGo = true;
        }
    }
}
