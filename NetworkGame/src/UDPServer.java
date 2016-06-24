
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

    byte[] sendBuffer = new byte[1024];
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;
    private int portnumber;
    private String hostname;

    private HashMap<InetAddress, Integer> IPlist = new HashMap<>();
    private List<UDPClient> clientList = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    public UDPServer(String hostname, int portnumber) {

        try {
            this.hostname = hostname;
            this.portnumber = portnumber;

            socket = new DatagramSocket(portnumber);

        } catch (SocketException e) {
            System.out.println("No socket available! " + e.getMessage());
        }
    }

    public void startServer(){

        while (true) {

            waitClientConnection();

            sendToAll();

            roomFull();
        }
    }

    private void waitClientConnection() {

        while(clientList.size() < 2){
            try {

                DatagramPacket receiveClient = new DatagramPacket(recvBuffer, recvBuffer.length);
                socket.receive(receiveClient);

                //TODO missing position in UDP client
                UDPClient clientConnection = new UDPClient(receiveClient.getAddress(),receiveClient.getPort(),clientList.size()+1,this);
                clientList.add(clientConnection);
                IPlist.put(receiveClient.getAddress(),receiveClient.getPort());
                pool.submit(clientConnection);

                System.out.println("### " + receiveClient.getAddress() + ":" + receiveClient.getPort() + " has connected.");

            } catch (IOException e) {
                System.out.println("ERROR Receiving! " + e.getMessage());
            }
        }
    }

    public void gameLogic(UDPClient client,String command) {

        switch (command) {
            case "up":
            case "down":
            case "left":
            case "right":
                clientList.get(client).move(command);
                break;
            case "attack":
                System.out.println("attacking");
                checkCollision(clientList.get(client));
                break;
            case "dead":
                clientList.remove(client);
                break;
        }
    }

    private void checkCollision(UDPClient player) {

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
/*
        String jsonArray = "";

        for (int i = 0; i < clientList.size();i++){
            jsonArray += clientList.get(i).toString() + ",";
        }
        sendBuffer = jsonArray.getBytes();

        for (UDPClient client:clientList) {
            client.send(sendBuffer);
        }
*/
        for (int i = 0; i < clientList.size();i++){
            for (int j = 0; j < clientList.size();j++){
                clientList.get(i).send(clientList.get(j).toString());
            }
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
}
