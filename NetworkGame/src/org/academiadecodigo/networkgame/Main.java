package org.academiadecodigo.networkgame;

import org.academiadecodigo.networkgame.Game.UDPGame;
import org.academiadecodigo.networkgame.Server.UDPServer;

/**
 * Created by codecadet on 26/06/16.
 */
public class Main {
    public static void main(String[] args) {

        /**
         * if the first argument is server it will create a server with the second argument passed
         */
        if(args[0].equals("server")){
            UDPServer server = new UDPServer(Integer.parseInt(args[1]));
            server.startServer();
        }

        /**
         * if the first argument is game it will create a game that will connect to a respective number and port
         */
        if(args[0].equals("game")){
            UDPGame game = new UDPGame(args[1], Integer.parseInt(args[2]));
            game.start();
        }
    }
}
