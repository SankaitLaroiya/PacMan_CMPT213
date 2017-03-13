package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Uses arrow keys for game input.
 */
@SuppressWarnings("serial")
public class InputController extends JPanel {
    private HashMap keyMap;
    private Maze gameMaze;

    // Names of arrow key actions.
    private static final String[] KEYS = {"UP", "DOWN", "LEFT", "RIGHT", "M", "H"};

    public InputController(Maze gameMaze) {
        this.gameMaze = gameMaze;
        int width = gameMaze.getMazeWidth();

        keyMap = new HashMap();
        keyMap.put("UP", -1 * width);
        keyMap.put("DOWN", width);
        keyMap.put("LEFT", -1);
        keyMap.put("RIGHT", 1);

        //ADDING ONLY TO PREVENT NULL POINTER EXCEPTIONS. JUST IN CASE.
        keyMap.put("M", 0);
        keyMap.put("H", 0);

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
                switch (move) {
                    case "M":
                        MazeGame.uncoverMaze(gameMaze);
                        MazeUI.printMaze(gameMaze.getMazeView(), gameMaze.getMazeWidth());
                        break;
                    case "H":
                        MazeUI.showGameMenu(false);
                        break;

                    default:
                        MazeActorController.movePlayer((int)keyMap.get(move), gameMaze);
                        MazeActorController.moveCats(gameMaze);
                        MazeUI.printMaze(gameMaze.getMazeView(), gameMaze.getMazeWidth());
                        break;
                }
            }
        };
    }
}
