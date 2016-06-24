import java.io.IOException;
import java.net.*;

public class UDPClient implements Runnable {

    private String name;
    private Position pos;
    private int speed = 2;
    private int health;
    private int strength;
    private boolean dead = false;
    private boolean hasAttacked = false;
    private UDPServer server;
    private int clientListSize;
    private InetAddress address;
    private int port;
    private String playerCommand;

    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    public UDPClient(InetAddress address, int port, Position pos, int clientlistsize, UDPServer server) {
        this.address = address;
        this.port = port;
        this.pos = pos;
        this.clientListSize = clientlistsize;
        this.server = server;

    }

    public UDPClient(){
        System.out.println(this);
    }

    public void dialogue() {


        while (true) {

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
            DatagramPacket receiveJSON = new DatagramPacket(recvBuffer, recvBuffer.length);

            try {
                socket.receive(receiveJSON);
                playerCommand = recvBuffer.toString();
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

        server.sendToAll();
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

    public void send(String UPCLientToString) {

        try {

            sendBuffer = UPCLientToString.getBytes();
            DatagramPacket sendJSON = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(sendJSON);

        } catch (IOException e) {
            System.out.println("Error" +e.getMessage());
        }
    }

    
    @Override
    public String toString() {
        return "{"+name+","+pos.getCol()+","+pos.getRow()+","+health+","+strength+","+dead+","+hasAttacked+"}";
    }
}
