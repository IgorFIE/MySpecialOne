package org.academiadecodigo.networkgame.Server;

import java.io.IOException;
import java.net.*;

public class UDPClient implements Runnable {

    private String name;
    private Position pos;
    private final int speed = 2;
    private final int strength = 2;
    private int health = 5;
    private boolean dead = false;
    private boolean hasAttacked = false;
    private boolean canMove = true;
    private UDPServer server;
    private InetAddress address;
    private int port;
    private String message;
    private byte[] sendBuffer;
    private byte[] recvBuffer = new byte[1024];
    private DatagramSocket socket = null;

    /**
     * UDPClient constructor
     *
     * @param address of the client
     * @param port    of the client
     * @param socket  for the communication between the client and player
     * @param server  the server that instantiate this Thread
     * @throws SocketException return Exception if socket is null
     */
    public UDPClient(InetAddress address, int port, DatagramSocket socket, UDPServer server) throws SocketException {
        this.address = address;
        this.port = port;
        this.server = server;
        this.socket = socket;
    }

    /**
     * while the player is connected
     * if is alive and can move it will recieve the
     * input from the player make actions and the send the
     * new updated status to all
     * <p>
     * if is dead the Thread is interrupted
     */
    @Override
    public void run() {

        while (true) {
            while (!dead) {
                System.out.println(address);
                while (canMove) {
                    playerAction(dialogue());
                    server.sendToAll();
                }
            }
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This method will wait for input of the player
     * then processes that input to a string
     * and return that string
     *
     * @return the input that a player has typed
     */
    private String dialogue() {

        //TODO - Don't know what the fuck i'm doing lol! NVM Check this sh!t
        DatagramPacket receiveMessage = new DatagramPacket(recvBuffer, recvBuffer.length);
        try {
            socket.receive(receiveMessage);

            message = new String(recvBuffer, 0, receiveMessage.getLength());

        } catch (IOException e) {
            System.out.println("ERROR Receiving! " + e.getMessage());
        }
        return message;
    }

    /**
     * filters the input received
     *
     * @param command receives a String with the action to process
     */
    private void playerAction(String command) {

        switch (command.toLowerCase()) {
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

    /**
     * Processes the input
     *
     * @param direction to move
     */
    private void move(String direction) {

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
                if (pos.getCol() + getSpeed() > server.FIELD_WIDTH) {
                    pos.setCol(server.FIELD_WIDTH);
                } else {
                    pos.setCol(pos.getCol() + getSpeed());
                }
                break;
        }
    }

    /**
     * Attack if the client didn´t attack in its actual position
     *
     * @return the damage to deal
     */
    public int attack() {

        if (!hasAttacked) {
            hasAttacked = true;
            return strength;
        }
        return 0;
    }

    /**
     * This method takes a amount of damage from the client
     *
     * @param dmg damage to take
     */
    public void loseHealth(int dmg) {
        System.out.println("lost health");
        health -= dmg;
        System.out.println("current health" + health);
        if (health <= 0) {
            health = 0;
            dead = true;
            canMove = false;
            if (server.getGameMode() == 0) {
                server.removeClient(this);
            }
            server.sendToAll();
        }
    }

    /**
     * Receives a String with all the JSONs from all clients
     * then send it to the player
     *
     * @param UPCLientToString the JSON to send to the player
     */
    public void send(String UPCLientToString) {

        try {
            sendBuffer = UPCLientToString.getBytes();
            DatagramPacket sendJSON = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(sendJSON);

        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    /**
     * compile the Status of the client and send it in the format JSON
     *
     * @return the JSON of the Client
     */
    @Override
    public String toString() {
        return "{name:" + address + "," +
                "col:" + pos.getCol() + "," +
                "row:" + pos.getRow() + "," +
                "health:" + health + "," +
                "dead:" + dead + "," +
                "hasAttacked:" + hasAttacked + "}";
    }

    //GETTERS AND SETTERS

    public int getSpeed() {
        return speed;
    }

    public Position getPos() {
        return pos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
