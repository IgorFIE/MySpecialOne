import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by codecadet on 24/06/16.
 */
public class UDPLocalPlayer implements Runnable{

    private String name;
    private BufferedReader input;
    private InetAddress hostAddress;
    private int port;
    private DatagramSocket socket;

    public UDPLocalPlayer (InetAddress hostAddress,int port,DatagramSocket gameSocket) {
        this.hostAddress = hostAddress;
        this.port = port;
        socket = gameSocket;
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            System.out.println("Enter your name!!!");
            name = input.readLine();
            System.out.println(name);
            byte[] bytesToSend = name.getBytes();
            DatagramPacket packet = new DatagramPacket(bytesToSend, bytesToSend.length, hostAddress, port);
            socket.send(packet);

            while(true){

                String action = input.readLine();
                    bytesToSend = action.getBytes();
                    packet = new DatagramPacket(bytesToSend, bytesToSend.length, hostAddress, port);
                    socket.send(packet);
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

    public String getAddress() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

