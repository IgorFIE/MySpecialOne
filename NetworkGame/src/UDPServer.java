
import java.io.IOException;
import java.net.*;


public class UDPServer {

        byte[] sendBuffer;
        byte[] recvBuffer = new byte[1024];

        DatagramSocket socket = null;

    public UDPServer(String hostname, int portnumber){

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
                System.out.println(new String(recvBuffer,0,receiveJSON.getLength()));

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


        }
    }
}
