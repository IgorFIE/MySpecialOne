import java.io.IOException;
import java.net.*;

public class UDPClient implements Runnable{

    private String name;
    private Position pos;
    private int speed = 2;
    private int health;
    private int strength;
    private boolean dead;
    private boolean hasAttacked;

    String hostName;
    int portNumber;

    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    public UDPClient(String hostname, int portnumber) {
        this.hostName = hostname;
        this.portNumber = portnumber;
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
                System.out.println("Don't know or can't connect to host!");
                e.printStackTrace();
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
                String receiveFromUDPClient = recvBuffer.toString();
                System.out.println(new String(recvBuffer, 0, receiveJSON.getLength()));
            } catch (IOException e) {
                System.out.println("ERROR Receiving! " + e.getMessage());
            }

        }

    }

    public int attack() {

        if (!hasAttacked) {
            hasAttacked = true;
            return strength;
        }
        return 0;
    }

    public void loseHealth(int dmg) {
        System.out.println("lost health");
        health -= dmg;
        System.out.println("current health" + health);
        if (health <= 0) {
            health = 0;
            dead = true;
        }
    }

    public void move(String direction) {

        hasAttacked = false;

        switch (direction) {

            case "up":
                if (getPos().getRow() - getSpeed() < 0) {
                    getPos().setRow(0);
                } else {
                    getPos().setRow(getPos().getRow() - getSpeed());
                }
                break;

            case "down":
                if (getPos().getRow() + getSpeed() > Field.height - 1) {
                    getPos().setRow(Field.height - 1);
                } else {
                    getPos().setRow(getPos().getRow() + getSpeed());
                }
                break;

            case "left":
                if (getPos().getCol() - getSpeed() < 0) {
                    getPos().setCol(0);
                } else {
                    getPos().setCol(getPos().getCol() - getSpeed());
                }
                break;

            case "right":
                if (getPos().getCol() + getSpeed() > Field.width - 1) {
                    getPos().setCol(Field.width - 1);
                } else {
                    getPos().setCol(getPos().getCol() + getSpeed());
                }
                break;

        }
    }

    public boolean isDead() {
        return dead;
    }

    public int getSpeed() {
        return speed;
    }

    public Position getPos() {
        return pos;
    }

    public boolean hasAttacked() {

        return hasAttacked;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {

    }
}
