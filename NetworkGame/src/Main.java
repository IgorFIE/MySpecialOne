import java.io.IOException;

/**
 * Created by codecadet on 20/06/16.
 */
public class Main {
    public static void main(String[] args) {
        Game game = null;
        try {
            game = new Game();
        } catch (IOException e) {
            e.printStackTrace();
        }
        game.start(2);
    }
}
