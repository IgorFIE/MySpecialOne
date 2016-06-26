
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
    private boolean isFull;
    private int gameMode;

    public final int FIELD_HEIGHT = 24;
    public final int FIELD_WIDTH = 99;

    private HashMap<InetAddress, Integer> IPlist = new HashMap<>();
    private List<UDPClient> clientList = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService pool = Executors.newFixedThreadPool(20);

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

            System.out.println("set positions and game mode:" + gameMode);
            gameMode();

            sendToAll();

            roomFull();
        }
    }

    private synchronized void waitClientConnection(){


        Thread waitClientConnection = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        DatagramPacket receiveClient = new DatagramPacket(recvBuffer, recvBuffer.length);
                        socket.receive(receiveClient);
                        String message = new String(recvBuffer, 0, receiveClient.getLength());

                        //TODO missing position in UDP client
                        UDPClient clientConnection = new UDPClient(receiveClient.getAddress(), receiveClient.getPort(), clientList.size() + 1, new DatagramSocket(), getServer());
                        clientConnection.setName(message);
                        clientList.add(clientConnection);
                        IPlist.put(receiveClient.getAddress(), receiveClient.getPort());
                        pool.submit(clientConnection);

                        System.out.println("### " + receiveClient.getAddress() + ":" + receiveClient.getPort() + " has connected.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        waitClientConnection.start();

        long time = System.currentTimeMillis() / 1000;
        long relativeTime = 0;

        while(((relativeTime - time)%100) < 25 && clientList.size() < 21){

            relativeTime = System.currentTimeMillis() / 1000;

            System.out.println("Time: " + (relativeTime-time)%60);
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        waitClientConnection.interrupt();
        isFull = true;
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
        while (isFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(UDPClient client) {
        clientList.remove(client);
        isFull = false;
        notify();
    }

    private void gameMode(){
        gameMode = ((int)(Math.random()*1));

        if(gameMode == 0){
            mySpecialGenerator(2,1,2);
        } else {
            createPositions();
        }
    }

    private void createPositions(){

        switch (clientList.size()){
            case 1:
            case 2:
                mySpecialGenerator(2,1,2);
                break;
            case 3:
            case 4:
                mySpecialGenerator(2,2,1);
                break;
            case 5:
            case 6:
                mySpecialGenerator(3,2,1);
                break;
            case 7:
            case 8:
                mySpecialGenerator(4,2,1);
                break;
            case 9:
            case 10:
                mySpecialGenerator(5,2,1);
                break;
            case 11:
            case 12:
                mySpecialGenerator(4,3,1);
                break;
            case 13:
            case 14:
                mySpecialGenerator(7,2,1);
                break;
            case 15:
            case 16:
                mySpecialGenerator(4,4,1);
                break;
            case 17:
            case 18:
                mySpecialGenerator(6,3,1);
                break;
            case 19:
            case 20:
                mySpecialGenerator(5,4,1);
                break;
        }
    }

    private void mySpecialGenerator(int cols, int rows, int a){

        int count = 0;

        for(int i = 0 ; i < rows;i++){
            double row = (2.*i+1.)/(double) (clientList.size());
            System.out.println("row:" + row);
            for (int j = 0; j < cols;j++){
                double col = (2.*j+1.)/(double) (a*clientList.size());
                System.out.println("col:" + col);
                if(count < clientList.size()) {
                    System.out.println(row + ":" + col);
                    clientList.get(count).setPos(new Position((int)Math.floor(col * FIELD_WIDTH), (int)Math.floor(row * FIELD_HEIGHT)));
                }
                count++;
            }
        }
    }

    public UDPServer getServer() {
        return this;
    }

    public int getGameMode() {
        return gameMode;
    }
}
