import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private static int portNumber = 8080;
    private static String hostName = "127.0.0.1";
    private static Socket socket;
    private static BufferedReader in;
    private Thread playerThread;
    private HashMap<String, Player> players;
    private LocalPlayer localPlayer;
    private String myNumber;

    // create a game object class from wich player extends
    // hashmap with key as player name and game object as value
    // input tells what kind of action is to be performed to wich player

    public static void main(String[] args) {

        try {
            Game game = new Game();
            String str;

            System.out.println(in.readLine());
            game.myNumber = in.readLine();
            System.out.println(game.myNumber);
            String playerNames = in.readLine();

            str = in.readLine();
            System.out.println(str);
            if (str.equals("start")) {
                System.out.println("started game");
                game.start(playerNames);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game() throws IOException {

        socket = new Socket(hostName, portNumber);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        localPlayer = new LocalPlayer(socket);
        playerThread = new Thread(localPlayer);
        players = new HashMap<>();
        Field.init(100, 25);
    }

    public void start(String playerNames) {

        createPlayers(playerNames);
        Field.draw(players);
        String[] input;
        String player;
        String action;

        try {
            String line;
            while (!socket.isClosed() && (line = in.readLine()) != null) {



                System.out.println(line);          // TODO put get input from stream in a separate method

                    if (line.equals("terminate")) {
                        break;
                    }
                    // use switch to check action and use name to get player and update property
                    input = line.split((":"));
                    player = input[0];
                    if(input.length<=1){
                        action = "slip";
                    } else {
                        action = input[1];
                    }

                switch (action) {               //TODO put this in a separate method

                    case "up":
                    case "down":
                    case "left":
                    case "right":
                        //players.get(player).move(input[1]);
                        break;
                    case "attack":
                        System.out.println("attacking");
                        checkCollision(players.get(player));
                        break;
                    case "dead":
                        players.remove(player);
                        break;

                }
                Field.draw(players);
            }

            System.out.println("you have died, you have been disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void checkCollision(Player player) {

        LinkedList<String> names = new LinkedList<>();
        names.addAll(players.keySet());

        for (int i = 0; i < names.size(); i++) {
            Player p2 = players.get(names.get(i));
            if (p2 == player) {
                continue;

            } else if (checkCrash(player.getPos(), p2.getPos())) {
                p2.loseHealth(player.attack());
            }
        }
    }

    private boolean checkCrash(Position pos1, Position pos2) {
        return Math.abs(pos1.getOutCol() - pos2.getOutCol()) <= pos1.getWidth() / 2 + pos2.getWidth() / 2 &&
                Math.abs(pos1.getOutRow() - pos2.getOutRow()) <= pos1.getHeight() / 2 + pos2.getHeight() / 2;
    }

    private void createPlayers(String playerNames) { // which is local player's name

        String[] names = playerNames.split(":");
        players = new HashMap<>(names.length);

        for (int i = 0; i < names.length; i++) {

           if (myNumber.equals(Integer.toString(i+1))) {
                players.put(names[i], localPlayer);
               localPlayer.getPos().setCol(i);
               localPlayer.getPos().setRow(i);

            } else {
                // TODO: 23/06/16 fix this, send position when create player
                players.put(names[i], new Player(new Position(i, i)));
            }
        }
    }

    private void close() {
        try {
            playerThread.interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
