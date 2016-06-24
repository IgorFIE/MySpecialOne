
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class UDPServer {

    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    private HashMap<String, Integer> IPlist;
    private List<UDPClient> clientList = Collections.synchronizedList(new LinkedList<>());


    public UDPServer(String hostname, int portnumber) {

        try {
            socket = new DatagramSocket(portnumber);
        } catch (SocketException e) {
            System.out.println("No socket available! " + e.getMessage());
        }


        while (true) {

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t 
            DatagramPacket receiveJSON = new DatagramPacket(recvBuffer, recvBuffer.length);

            try {


                socket.receive(receiveJSON);
                String receiveFromUPDServer = recvBuffer.toString();
                System.out.println(new String(recvBuffer, 0, receiveJSON.getLength()));

            } catch (IOException e) {
                System.out.println("ERROR Receiving! " + e.getMessage());
            }

            sendBuffer = new String("Send to UDPServer").getBytes();
            DatagramPacket sendJSON = new DatagramPacket(sendBuffer, sendBuffer.length, receiveJSON.getAddress(), receiveJSON.getPort());

            try {
                socket.send(sendJSON);
            } catch (IOException e) {
                System.out.println("ERROR Sending! " + e.getMessage());
            }

            // TODO: 24/06/16 before game start, assign positions to all players and ask for their json

        }
    }

    public void waitClientConnection() {

    }

    public synchronized void gameLogic(String command) {

        switch (action) {               //TODO put this in a separate method

            case "up":
            case "down":
            case "left":
            case "right":
                players.get(player).move(input[1]);
                break;
            case "attack":
                System.out.println("attacking");
                checkCollision(players.get(player));
                break;
            case "dead":
                players.remove(player);
                break;

        }
    }

    public void sendToAll() {

    }

    public void removeClient() {

    }



}
