package org.academiadecodigo.networkgame.Server;

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

    byte[] recvBuffer = new byte[1024];
    DatagramSocket socket = null;

    private boolean isFull;
    private int gameMode;

    public final int FIELD_HEIGHT = 24;
    public final int FIELD_WIDTH = 99;

    private HashMap<InetAddress, Integer> IPlist = new HashMap<>();
    private List<UDPClient> clientList = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService pool = Executors.newFixedThreadPool(20);

    /**
     * Constructor receives a port
     *
     * @param portNumber of the DatagramSocket to wait for connections
     */
    public UDPServer(int portNumber) {
        try {
            socket = new DatagramSocket(portNumber);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the Server loop
     * wait for 50 seconds for players
     * then choose the game mode to play
     * then send all JSONS to all players
     * then wait while the room is full
     */
    public void startServer(){

        while (true) {

            waitClientConnection();

            gameMode();
            System.out.println("set positions and game mode: " + gameMode);

            sendToAll();

            roomFull();
        }
    }

    /**
     * This method creates a Thread to wait for client connections
     * and while the number of players is lower then 20 and the time is lower then 50 seconds
     * it will wait for clients
     *
     * then the Thread is interrupted and the boolean isFull is set to true
     */
    private synchronized void waitClientConnection(){

        Thread waitClientConnection = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        DatagramPacket receiveClient = new DatagramPacket(recvBuffer, recvBuffer.length);
                        socket.receive(receiveClient);
                        String message = new String(recvBuffer, 0, receiveClient.getLength());

                        UDPClient clientConnection = new UDPClient(receiveClient.getAddress(), receiveClient.getPort(), new DatagramSocket(), getServer());
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

        while(((relativeTime - time)%100) < 50 && clientList.size() < 21){

            relativeTime = System.currentTimeMillis() / 1000;

            System.out.println("Time: " + (relativeTime-time)%100);
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        waitClientConnection.interrupt();
        isFull = true;
    }

    /**
     * defines the game mode
     *
     * 0 = 1 vs 1 arena
     * 1 = last man standing
     */
    private void gameMode(){
        gameMode = ((int)(Math.random()*2));

        if(gameMode == 0){
            clientList.get(0).setPos(new Position(33,12));
            clientList.get(1).setPos(new Position(82,12));

            for(int i = 2; i < clientList.size();i++){
                clientList.get(i).setPos(new Position(-1,-1));
                clientList.get(i).setCanMove(false);
            }
        } else {
            createPositions();
        }
    }

    /**
     * checks the clientList size then for that number
     * it will generates differents positions
     */
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

    /**
     * Generates the positions to all players
     * by the values received
     *
     * @param cols to insert players
     * @param rows to insert players
     * @param a coefficient
     */
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

    /**
     * while the room is full it will wait
     */
    private synchronized void roomFull() {
        while (isFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * it will remove a player then set others 2 players
     * int the field
     *
     * this only affects in gameMode 0
     *
     * @param client to remove from the list
     */
    public synchronized void removeClient(UDPClient client) {

        clientList.remove(client);

        clientList.get(0).setPos(new Position(33,12));
        clientList.get(0).setCanMove(true);
        clientList.get(1).setPos(new Position(82,12));
        clientList.get(1).setCanMove(true);

        if(clientList.size() == 1) {
            clientList.remove(0);
            isFull = false;
            notify();
        }
    }


    /**
     * Send to all will collect all the JSONS from all clients
     * Then send to all clients
     */
    public void sendToAll() {

        String jsonArray = "";

        for (int i = 0; i < clientList.size();i++){
            jsonArray += clientList.get(i).toString() + ";";
        }

        for (UDPClient client:clientList) {
            client.send(jsonArray);
        }
    }

    /**
     * This receives a client then it will compare its position
     * with all the others clients property's
     *
     * @param player to compare
     */
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

    /**
     * check if the players are in the same area
     *
     * @param pos1 Position from one player
     * @param pos2 Position from one player
     * @return a boolean
     */
    private boolean checkCrash(Position pos1, Position pos2) {
        return Math.abs(pos1.getOutCol() - pos2.getOutCol()) <= pos1.getWidth() / 2 + pos2.getWidth() / 2 &&
                Math.abs(pos1.getOutRow() - pos2.getOutRow()) <= pos1.getHeight() / 2 + pos2.getHeight() / 2;
    }

    //GETTERS

    public UDPServer getServer() {
        return this;
    }

    public int getGameMode() {
        return gameMode;
    }
}
