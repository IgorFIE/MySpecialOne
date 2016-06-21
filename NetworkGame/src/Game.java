import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private Position[] positions;

    private static int portNumber = 8080;
    private static String hostName = "127.0.0.1";
    private static Socket socket;
    private static BufferedReader in;
    private Thread playerThread;
    private HashMap<String, Player> players;


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

            str = in.readLine();
            System.out.println(str);
            if (str.equals("start")) {
                System.out.println("started game");
                game.start(2);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game() throws IOException {

        socket = new Socket(hostName, portNumber);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        playerThread = new Thread(new LocalPlayer(socket));
        players = new HashMap<>();
        Field.init(100, 25);
    }

    public void start(int numPlayers) {
        createPlayers(numPlayers);

        String[] input;
        String player;
        String action;

        try {
            while (true) {

                String line = in.readLine();

                if (line != null) {
                    System.out.println(line);

                    if (line.equals("terminate")) {
                        break;
                    }
                    // use switch to check action and use name to get player and update property
                    input = line.split((":"));
                    player = input[0];
                    action = input[1];

                    switch (action) {

                        case "up":
                        case "down":
                        case "left":
                        case "right":
                            players.get(player).move(input[1]);
                            break;
                        case "attack":
                            //detect nearby enemies, deal damage
                            break;
                        case "die":
                            //remove player, kill thread(?)
                            break;
                    }
                }

                Field.draw(players);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void createPlayers(int numPlayers) {
        positions = new Position[numPlayers];
        positions[0] = new Position(0, Field.height / 2);
        positions[1] = new Position(Field.width / 2, 0);
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
