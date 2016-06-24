import java.io.IOException;
import java.net.*;

public class UDPClient {

    String hostName;
    int portNumber;

    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    public UDPClient (String hostname, int portnumber){
        this.hostName = hostname; this.portNumber = portnumber;
    }

    public void dialogue() {

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("No socket available! " + e.getMessage());
        }

        while (true) {

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
            DatagramPacket sendJSON = null;

            try {
                sendBuffer = new String("Send to UDPclient").getBytes();
                sendJSON = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(hostName), portNumber);
            } catch (UnknownHostException e) {
                System.out.println("Don't know or can't connect to host!");e.printStackTrace();
            }

            try {
                socket.send(sendJSON);
            } catch (IOException e) {
                System.out.println("ERROR Sending! " + e.getMessage());
            }

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
            DatagramPacket receiveJSON = new DatagramPacket(recvBuffer, recvBuffer.length);

            try {
                socket.receive(receiveJSON);
                String receiveFromUDPClient=recvBuffer.toString();
                System.out.println(new String(recvBuffer, 0, receiveJSON.getLength()));
            } catch (IOException e) {
                System.out.println("ERROR Receiving! " + e.getMessage());
            }

        }

    }

}
