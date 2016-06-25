import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by codecadet on 24/06/16.
 */
public class UDPLocalPlayer implements Runnable{

    private String name;
    BufferedReader input;
    InetAddress hostAddress;
    int port;
    DatagramSocket socket;


    public UDPLocalPlayer (InetAddress hostAddress,int port) {
        this.hostAddress = hostAddress;
        this.port = port;
        try {
            socket = new DatagramSocket();
            input = new BufferedReader(new InputStreamReader(System.in));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("im here!!!");
            name = input.readLine();
            System.out.println(name);
            byte[] bytesToSend = name.getBytes();
            DatagramPacket packet = new DatagramPacket(bytesToSend, bytesToSend.length, hostAddress, port);
            socket.send(packet);

            while(true){

                String action = input.readLine();
                if(action != null && action.toLowerCase().matches("[up][down][left][right][attack]")) {
                    System.out.println(action);
                    bytesToSend = action.getBytes();
                    packet = new DatagramPacket(bytesToSend, bytesToSend.length, hostAddress, port);
                    socket.send(packet);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}

