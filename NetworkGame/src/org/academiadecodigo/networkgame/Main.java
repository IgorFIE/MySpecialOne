package org.academiadecodigo.networkgame;

import org.academiadecodigo.networkgame.Game.UDPGame;
import org.academiadecodigo.networkgame.Server.UDPServer;

/**
 * Created by codecadet on 26/06/16.
 */
public class Main {
    public static void main(String[] args) {
        if(args[0].equals("server")){
            UDPServer server = new UDPServer(args[1], Integer.parseInt(args[2]));
            server.startServer();
        }

        if(args[0].equals("game")){
            UDPGame game = new UDPGame(args[1], Integer.parseInt(args[2]));
            game.start();
        }
    }
}
