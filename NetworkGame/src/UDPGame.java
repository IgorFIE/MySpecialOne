import java.io.IOException;
import java.net.*;

/**
 * Created by codecadet on 24/06/16.
 */
public class UDPGame {

    public static void main(String[] args) {
        try {
            UDPGame game = new UDPGame();
            game.start();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    private DatagramSocket gameSocket;
    private byte[] receiveData = new byte[1024];

    public UDPGame() throws UnknownHostException, SocketException {

        Thread thead = new Thread(new UDPLocalPlayer(InetAddress.getByName("localhost"),8080));
        thead.start();
        gameSocket = new DatagramSocket();
    }

    private void start () {

        try {
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                gameSocket.receive(receivePacket);

                String message = new String(receivePacket.getData());

                parseJSON(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String JSON) {
        System.out.println(JSON);
    }
}
