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
    private int playerNumber;
    private InetAddress address;
    private int port;
    private String message;
    private final int FIELD_HEIGHT = 25;
    private final int FIELD_WIDTH = 100;
    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    public UDPClient(InetAddress address, int port, int playerNumber,DatagramSocket socket, UDPServer server) throws SocketException {
        this.address = address;
        this.port = port;
        this.playerNumber = playerNumber;
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {

        while(true) {
            playerAction(dialogue());
            server.sendToAll();
        }
    }

    public String dialogue() {

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
            DatagramPacket receiveJSON = new DatagramPacket(recvBuffer, recvBuffer.length);
            try {
                socket.receive(receiveJSON);

                System.out.println(receiveJSON.getAddress());

                message = new String(recvBuffer, 0, receiveJSON.getLength());

                System.out.println(message);

            } catch (IOException e) {
                System.out.println("ERROR Receiving! " + e.getMessage());
            }
        return message;
    }

    public void playerAction(String command) {

        switch (command) {
            case "up":
            case "down":
            case "left":
            case "right":
                move(command);
                break;
            case "attack":
                server.checkCollision(this);
                break;
            case "dead":
                server.removeClient(this);
                break;
        }
    }

    public void move(String direction) {

        hasAttacked = false;

        switch (direction) {
            case "up":

                if (pos.getRow() - getSpeed() < 0) {
                    pos.setRow(0);
                } else {
                    pos.setRow(pos.getRow() - getSpeed());
                }
                break;

            case "down":

                if (pos.getRow() + getSpeed() > FIELD_HEIGHT - 1) {
                    pos.setRow(Field.height - 1);
                } else {
                    pos.setRow(pos.getRow() + getSpeed());
                }
                break;

            case "left":
                if (pos.getCol() - getSpeed() < 0) {
                    pos.setCol(0);
                } else {
                    pos.setCol(pos.getCol() - getSpeed());
                }
                break;

            case "right":
                if (pos.getCol() + getSpeed() > FIELD_WIDTH - 1) {
                    pos.setCol(Field.width - 1);
                } else {
                    pos.setCol(pos.getCol() + getSpeed());
                }
                break;
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
        return "{name:"+ name+ "," +
                "col:"+ pos.getCol() + "," +
                "row:"+ pos.getRow() + "," +
                "health:" + health + ","+
                "dead:" + dead + "," +
                "hasAttacked:" + hasAttacked + "}";
    }


    public void setPos(Position pos) {
        this.pos = pos;
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

    public void setName(String name) {
        this.name = name;
    }
}
