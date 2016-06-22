import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

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

    // create a game object class from wich player extends
    // hashmap with key as player name and game object as value
    // input tells what kind of action is to be performed to wich player

    public static void main(String[] args) {

        try {
            Game game = new Game();
            String str;

            System.out.println(in.readLine());
            str = in.readLine();
            System.out.println(str);
            String[] names = str.split(":");
            System.out.println(names.length);
            System.out.println(names);

            str = in.readLine();
            System.out.println(str);
            if (str.equals("start")) {
                System.out.println("started game");
                game.start(names);

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

    public void start(String[] names) {
        createPlayers(names);

        String[] input;
        String player;
        String action;

        try {
            String line;
            while ((line = in.readLine()) != null) {

                Field.draw(players);

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
                        players.get(player).move(input[1]);
                        break;
                    case "attack":
                        System.out.println("attacking");
                        players.get(player).attack();
                        break;
                    case "die":
                        //remove player, kill thread(?)
                        break;
                }
                checkCollision();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void checkCollision() {
        for (String name : players.keySet()) {
            Player p1 = players.get(name);
            
            for (String name2: players.keySet()) {
                Player p2 = players.get(name2);

                if(p2 == p1){ continue;}

                if(checkCrash(p1.getPos(), p2.getPos())) {
                    System.out.println("crashed");
/*                    p1.setCrashed();
                    p2.setCrashed();*/
                }
            }
        }
    }

    private boolean checkCrash(Position pos1, Position pos2) {
        return Math.abs(pos1.getOutCol() - pos2.getOutCol()) <= pos1.getWidth() / 2 + pos2.getWidth() / 2 &&
                Math.abs(pos1.getOutRow() - pos2.getOutRow()) <= pos1.getHeight() / 2 + pos2.getHeight() / 2;
    }

    private void createPlayers(String[] names) { // which is local player's name
        players.put(names[0], localPlayer);
        players.put(names[1], new Player(new Position(25, 25)));
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
