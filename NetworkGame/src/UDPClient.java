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
    private String playerCommand;

    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    public UDPClient(InetAddress address, int port, int playerNumber, UDPServer server) {
        this.address = address;
        this.port = port;
        this.playerNumber = playerNumber;
        this.server = server;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while(true) {
            playerAction(dialogue());
        }
    }

    public String dialogue() {

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
            DatagramPacket receiveJSON = new DatagramPacket(recvBuffer, recvBuffer.length);
            try {
                socket.receive(receiveJSON);
                playerCommand = recvBuffer.toString();
                System.out.println(playerCommand);
            } catch (IOException e) {
                System.out.println("ERROR Receiving! " + e.getMessage());
            }
        return playerCommand;
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
                System.out.println("attacking");
                server.checkCollision(this);
                break;
            case "dead":
                server.removeClient(this);
                break;
        }
        server.sendToAll();
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
                "hasAttacked:" + hasAttacked + "," +
                "playerNumber:" + playerNumber + "," + "}";
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
}
