
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer {

    public static void main(String[] args) {
        UDPServer server = new UDPServer("192.168.1.27", 8080);
        server.startServer();
    }

    byte[] sendBuffer = new byte[1024];
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;
    private int portnumber;
    private String hostname;

    private HashMap<InetAddress, Integer> IPlist = new HashMap<>();
    private List<UDPClient> clientList = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    public UDPServer(String hostname, int portnumber) {
            this.hostname = hostname;
            this.portnumber = portnumber;
        try {
            socket = new DatagramSocket(portnumber);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void startServer(){

        while (true) {

            waitClientConnection();

            createPositions();
            System.out.println("set positions");

            sendToAll();

            roomFull();
        }
    }

    private void waitClientConnection() {

        while(clientList.size() < 2){
            try {

                DatagramPacket receiveClient = new DatagramPacket(recvBuffer, recvBuffer.length);
                socket.receive(receiveClient);

                String message = new String(recvBuffer,0,receiveClient.getLength());

                //TODO missing position in UDP client
                UDPClient clientConnection = new UDPClient(receiveClient.getAddress(),receiveClient.getPort(),clientList.size()+1,this);
                clientConnection.setName(message);
                clientList.add(clientConnection);
                IPlist.put(receiveClient.getAddress(),receiveClient.getPort());
                pool.submit(clientConnection);

                System.out.println("### " + receiveClient.getAddress() + ":" + receiveClient.getPort() + " has connected.");

            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    public void checkCollision(UDPClient player) {

        for (int i = 0; i < clientList.size(); i++) {
            UDPClient p2 = clientList.get(i);
            if (p2 == player) {
                continue;

            } else if (checkCrash(player.getPos(), p2.getPos())) {
                p2.loseHealth(player.attack());
            }
        }
    }

    private boolean checkCrash(Position pos1, Position pos2) {
        return Math.abs(pos1.getOutCol() - pos2.getOutCol()) <= pos1.getWidth() / 2 + pos2.getWidth() / 2 &&
                Math.abs(pos1.getOutRow() - pos2.getOutRow()) <= pos1.getHeight() / 2 + pos2.getHeight() / 2;
    }

    public void sendToAll() {

        String jsonArray = "";

        for (int i = 0; i < clientList.size();i++){
            jsonArray += clientList.get(i).toString() + ";";
        }

        for (UDPClient client:clientList) {
            client.send(jsonArray);
        }

    }

    private synchronized void roomFull() {
        while (clientList.size() == 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(UDPClient client) {
        clientList.remove(client);
        notify();
    }

    private void createPositions(){

        clientList.get(0).setPos(new Position(12,33));
        clientList.get(1).setPos(new Position(12,82));

        /*Position pos = new Position(50,12);

        for(int i = 0; i < clientList.size();i++){
            clientList.get(i).setPos(pos);
        }*/
    }
}
