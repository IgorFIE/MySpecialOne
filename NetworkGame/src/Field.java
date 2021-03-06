import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.HashMap;


/**
 * Created by codecadet on 20/06/16.
 */
public class Field {

    public static int width;
    public static int height;

    // Used to write to screen
    private static com.googlecode.lanterna.screen.Screen screen;

    // Screen wrapper that preserves default options
    private static ScreenWriter screenWriter;

    //This class is not supposed to be instantiated
    private Field() {
    }

    /**
     * Initializes the Screen
     *
     * @param width  screen width
     * @param height screen height
     */
    public static void init(int width, int height) {

        // Create the GUI
        screen = TerminalFacade.createScreen();

        // Set field size
        Field.width = width;
        Field.height = height;
        screen.setCursorPosition(null);
        screen.getTerminal().setCursorVisible(false); // Not Working
        screen.getTerminal().getTerminalSize().setColumns(width);
        screen.getTerminal().getTerminalSize().setRows(height);

        // Default screen writing options
        screenWriter = new ScreenWriter(screen);
        screenWriter.setBackgroundColor(Terminal.Color.BLUE);
        screenWriter.setForegroundColor(Terminal.Color.WHITE);
        screen.startScreen();

    }

    public static void draw(HashMap<String, Player> players) {

        screen.clear();
        for (String s : players.keySet()) {
            int x = players.get(s).getPos().getCol();
            int y = players.get(s).getPos().getRow();

            if (players.get(s).hasAttacked()) {
                for (int i =  x-1; i <= x + 1; i++) {
                    for (int j = y-1; j <= y + 1; j++) {
                        screen.putString(i, j, " ", Terminal.Color.GREEN, Terminal.Color.RED, ScreenCharacterStyle.Bold);
                    }
                }
            }
            screen.putString(x, y, Integer.toString(players.get(s).getHealth()), Terminal.Color.BLACK, Terminal.Color.YELLOW, ScreenCharacterStyle.Bold);
        }
        screen.refresh();
    }
}
