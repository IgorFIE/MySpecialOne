package org.academiadecodigo.networkgame.Game;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

/**
 * Created by codecadet on 24/06/16.
 */
public class UDPGame {

    private DatagramSocket gameSocket;
    private UDPLocalPlayer player;
    private byte[] receiveData = new byte[1024];
    private HashMap<String,String[]> players = new HashMap<>();

    public UDPGame(String host) {

        try {
            gameSocket = new DatagramSocket();

            player = new UDPLocalPlayer(InetAddress.getByName(host),8080,gameSocket);
            Thread thead = new Thread(player);
            thead.start();

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            gameSocket.receive(receivePacket);

            player.setPort(receivePacket.getPort());

            String message = new String(receiveData, 0, receivePacket.getLength());

            parseJSON(message);

            Field.init(100, 25);

            Field.draw(players,player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start () {

        try {

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                gameSocket.receive(receivePacket);

                String message = new String(receiveData, 0, receivePacket.getLength());

                parseJSON(message);

                Field.draw(players,player);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String JSON) {

        String[] split = JSON.split(";");

        for(int i = 0;i < split.length;i++){
            String split2 = split[i].substring(1,split[i].length()-1);
            String[] split3 = split2.split(",");
            String name = "";
            String[] array = new String[split3.length-1];
            for(int j = 0; j < split3.length; j++) {
                if(j == 0){
                    name = split3[0].split(":")[1];
                    name = name.substring(1);
                } else{
                    array[j-1] = split3[j].split(":")[1];
                }
            }
            players.put(name,array);
        }

    }

}
