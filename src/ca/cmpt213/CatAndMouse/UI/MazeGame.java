package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static ca.cmpt213.CatAndMouse.Logic.MazeActorController.movePlayer;
import static ca.cmpt213.CatAndMouse.Logic.MazeActorController.playerPos;
import static ca.cmpt213.CatAndMouse.UI.MazeUI.printToScr;
import static ca.cmpt213.CatAndMouse.UI.MazeUI.printMaze;
import static ca.cmpt213.CatAndMouse.UI.MazeUI.showGameMenu;

/**
 * Cat and Mouse game's main activity class
 */
public class MazeGame {

    public static final Integer PLAYER_POS = 21;
    public static final Integer CAT1_POS = 38;
    public static final Integer CAT2_POS = 261;
    public static final Integer CAT3_POS = 278;

    private static final Integer UP = -20;
    private static final Integer DOWN = 20;
    private static final Integer LEFT = -1;
    private static final Integer RIGHT = 1;

    public static void main(String[] args) {
        Maze myMaze = new Maze();

        myMaze.constructMaze();

        showGameMenu(true);

        MazeActorController.initGameActors(myMaze);

        boolean gameLost = false;
        int numCheeseCollected = 0;
        char input;

        Scanner inputStream = new Scanner(System.in);

        while(!(gameLost || numCheeseCollected >= 5)) {

            printMaze(myMaze.getMazeView());
            printToScr("Enter your move [WASD?]: ");

            try {
                input = inputStream.nextLine().toLowerCase().charAt(0);

            } catch(StringIndexOutOfBoundsException s) {
                printToScr("Invalid input! Please try Again.\n");
                input = ' ';
                continue;
            }

            switch (input) {
                case 'w':
                    movePlayer(UP + playerPos, myMaze);
                    myMaze.revealFog(playerPos);
                    break;

                case 'a':
                    movePlayer(LEFT + playerPos, myMaze);
                    myMaze.revealFog(playerPos);
                    break;

                case 's':
                    movePlayer(DOWN + playerPos, myMaze);
                    myMaze.revealFog(playerPos);
                    break;

                case 'd':
                    movePlayer(RIGHT + playerPos, myMaze);
                    myMaze.revealFog(playerPos);
                    break;

                case 'm':
                    ArrayList<Character> altMaze = new ArrayList<>(myMaze.getMaze());
                    Collections.replaceAll(altMaze,'.', ' ');
                    Collections.replaceAll(myMaze.getMaze(),'.', ' ');

                    myMaze.setMazeView(altMaze);
                    break;

                case '?':
                    MazeUI.showGameMenu(false);
                    break;

                case 'e':
                    printToScr("Exiting...\n");
                    return;

                default:
                    printToScr("Invalid input! Please try Again.\n");
                    input = ' ';
            }
        }
    }
}
