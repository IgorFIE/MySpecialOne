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
    private boolean canMove;
    byte[] sendBuffer;
    byte[] recvBuffer = new byte[1024];

    DatagramSocket socket = null;

    public UDPClient(InetAddress address, int port, int playerNumber,DatagramSocket socket, UDPServer server) throws SocketException {
        this.address = address;
        this.port = port;
        this.playerNumber = playerNumber;
        this.server = server;
        this.socket = socket;
        this.health = 5;
        this.strength = 2;
        this.canMove = true;
    }

    @Override
    public void run() {

        while(true) {
            while (!dead) {
                while (canMove) {
                    playerAction(dialogue());
                    server.sendToAll();
                }
            }
            Thread.currentThread().interrupt();
        }
    }

    public String dialogue() {

            //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
            DatagramPacket receiveMessage = new DatagramPacket(recvBuffer, recvBuffer.length);
            try {
                socket.receive(receiveMessage);

                System.out.println(receiveMessage.getAddress());

                message = new String(recvBuffer, 0, receiveMessage.getLength());

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

                if (pos.getRow() + getSpeed() > server.FIELD_HEIGHT) {
                    pos.setRow(server.FIELD_HEIGHT);
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
                if (pos.getCol() + getSpeed() > server.FIELD_WIDTH ) {
                    pos.setCol(server.FIELD_WIDTH);
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
            if(server.getGameMode() == 0){
                server.removeClient(this);
            }
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
        return "{name:"+ playerNumber+ "," +
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

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
