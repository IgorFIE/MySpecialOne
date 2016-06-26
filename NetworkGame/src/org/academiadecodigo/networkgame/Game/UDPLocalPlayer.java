package org.academiadecodigo.networkgame.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by codecadet on 24/06/16.
 * Local player that connects to server and sends packets with user input.
 */
public class UDPLocalPlayer implements Runnable{

    private String name;
    private BufferedReader input;
    private InetAddress hostAddress;
    private int port;
    private DatagramSocket socket;

    /**
     *
     * @param hostAddress   InetAddress of the server to be connected with
     * @param port  Port number on which the server is listening
     * @param gameSocket    DatagramSocket from which the local player sends the packets
     */

    public UDPLocalPlayer (InetAddress hostAddress,int port,DatagramSocket gameSocket) {
        this.hostAddress = hostAddress;
        this.port = port;
        socket = gameSocket;
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Gets name from user and sends first packet to server with it.
     * Reads commands from keyboard and sends it to server.
     */

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

    /**
     *
     * @return  returns address from player
     */

    public String getAddress() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     *
     * @param port  New port to which it will send the commands.
     */

    public void setPort(int port) {
        this.port = port;
    }
}

