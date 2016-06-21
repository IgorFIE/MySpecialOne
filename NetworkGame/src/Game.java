import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    Field field;
    private Position[] positions;

    private static int portNumber = 8080;
    private static String hostName;
    private static Socket socket;
    private static BufferedReader in;
    Thread playerThread;

    // create a game object class from wich player extends
    // hashmap with key as player name and game object as value
    // input tells what kind of action is to be performed to wich player

    public static void main(String[] args) {

        try {
            Game game = new Game();

            String[] names = in.readLine().split(":");
            if(in.readLine().equals("start")) {
                game.start(2);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Game() throws IOException {

        socket = new Socket(hostName, portNumber);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        playerThread = new Thread(new Player(socket));
        Field.init(100, 25);

    }


    public void start(int numPlayers) {
        createPlayers(numPlayers);

        try {
            while (true) {

                String line = in.readLine();
                if (line.equals("terminate")) {
                    break;
                }
                if (line != null) {   // use switch to check action and use name to get player and update property
                    String[] input = line.split((":"));
                    String player = input[0];
                    int col = Integer.parseInt(input[1]);
                    int row = Integer.parseInt(input[2]);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void createPlayers(int numPlayers) {
        positions = new Position[numPlayers];
        positions[0] = new Position(0, field.height / 2);
        positions[1] = new Position(field.width / 2, 0);
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
