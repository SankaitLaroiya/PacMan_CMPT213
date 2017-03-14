package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Class to register key strokes and pass the appropriate input effect on
 * to MazeActorController and MazeGame class.
 */
@SuppressWarnings("serial")
public class MazeInputController extends JPanel {

    private HashMap keyMap;
    private Maze gameMaze;

    public static String lastInput = "right";

    // Names of arrow key actions.
    private static final String[] KEYS = {"UP", "DOWN", "LEFT", "RIGHT", "M", "H", "E"};

    public MazeInputController(Maze gameMaze) {
        this.gameMaze = gameMaze;
        int width = gameMaze.getMazeWidth();

        keyMap = new HashMap();
        keyMap.put("UP", -1 * width);
        keyMap.put("DOWN", width);
        keyMap.put("LEFT", -1);
        keyMap.put("RIGHT", 1);

        //ADDING ONLY TO PREVENT NULL POINTER EXCEPTIONS. IMPOSSIBLE BUT JUST IN CASE.
        keyMap.put("M", 0);
        keyMap.put("H", 0);
        keyMap.put("E", 0);

        registerKeyPresses();
    }

    public void registerKeyPresses() {
        for (int i = 0; i < KEYS.length; i++) {
            String key = KEYS[i];
            this.getInputMap().put(KeyStroke.getKeyStroke(key), key);
            this.getActionMap().put(key, getKeyListener(key));
        }
    }

    public AbstractAction getKeyListener(final String move) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                lastInput = move;

                switch (move) {
                    case "M":
                        MazeGame.uncoverMaze(gameMaze);
                        MazeGUI.playSound(MazeGUI.MAZE_REVEAL);
                        break;
                    case "H":
                        MazeTerminalUI.showGameMenu(false);
                        break;

                    case "E":
                        System.out.println("EXITING...");
                        System.exit(0);
                        break;

                    default:
                        //The win condition is checked regularly in ever move of the player and the cats.
                        //See movePlayer and moveCats methods for details.
                        MazeActorController.movePlayer((int)keyMap.get(move), gameMaze);
                        MazeActorController.moveCats(gameMaze);
                        break;
                }
            }
        };
    }
}
