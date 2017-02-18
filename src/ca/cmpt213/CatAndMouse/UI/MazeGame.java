package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static ca.cmpt213.CatAndMouse.Logic.MazeActorController.moveActors;
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


    public static int numCheeseCollected = 0;
    public static boolean gameLost = false;

    public static void main(String[] args) {
        Maze gameMaze = new Maze();

        gameMaze.constructMaze();

        showGameMenu(true);

        MazeActorController.initGameActors(gameMaze);

        char input;

        Scanner inputStream = new Scanner(System.in);

        gameMaze.revealFog(playerPos);

        while (!(gameLost || numCheeseCollected >= 5)) {
            MazeActorController.moveCats(gameMaze);
            MazeActorController.checkCondition(gameMaze);

            if(gameLost) {
                break;
            }

            printMaze(gameMaze.getMazeView());

            printToScr("Cheese collected: " + numCheeseCollected + " of 5\n");
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
                    moveActors(UP + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 'a':
                    moveActors(LEFT + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 's':
                    moveActors(DOWN + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 'd':
                    moveActors(RIGHT + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 'm':
                    clearMaze(gameMaze);
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

        if(numCheeseCollected >= 5){
            printToScr("\nCheese collected: " + numCheeseCollected + " of 5\n");
            printToScr("Congratulations. You've won!!");

            clearMaze(gameMaze);
            printMaze(gameMaze.getMazeView());
        }

        if(gameLost){
            printToScr("\nSorry, cat got your ton...um your mouse!\n");
            printToScr("Cheese collected: " + numCheeseCollected + " of 5\n");


            clearMaze(gameMaze);
            printMaze(gameMaze.getMazeView());
            printToScr("GAME OVER; please try again.");
        }
    }
    private static void clearMaze(Maze myMaze){
        //ArrayList<Character> altMaze = new ArrayList<>(myMaze.getMaze());
        //Collections.replaceAll(altMaze,'.', ' ');
        Collections.replaceAll(myMaze.getMaze(),'.', ' ');
        myMaze.setMazeView(myMaze.getMaze());
    }
}
