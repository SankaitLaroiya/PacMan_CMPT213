package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;

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
    public static boolean gameLost = false;
    public static int numCheeseCollected = 0;

    private static final Integer UP = -20;
    private static final Integer DOWN = 20;
    private static final Integer LEFT = -1;
    private static final Integer RIGHT = 1;

    public static void main(String[] args) {
        Maze gameMaze = new Maze(15, 20);
        char input;
        Scanner inputStream = new Scanner(System.in);

        gameMaze.constructMaze();
        MazeActorController.initGameActors(gameMaze);

        showGameMenu(true);

        gameMaze.revealFog(playerPos);
        printMaze(gameMaze.getMaze());

        /*while (!(gameLost || numCheeseCollected >= 5)) {
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
                    movePlayer(UP + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 'a':
                    movePlayer(LEFT + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 's':
                    movePlayer(DOWN + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 'd':
                    movePlayer(RIGHT + playerPos, gameMaze);
                    gameMaze.revealFog(playerPos);
                    break;

                case 'm':
                    uncoverMaze(gameMaze);
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

            uncoverMaze(gameMaze);
            printMaze(gameMaze.getMazeView());
        }

        if(gameLost){
            printToScr("\nSorry, cat got your ton...um your mouse!\n");

            uncoverMaze(gameMaze);
            printMaze(gameMaze.getMazeView());

            printToScr("Cheese collected: " + numCheeseCollected + " of 5\n");
            printToScr("GAME OVER; please try again.");
        }*/
    }

    private static void uncoverMaze(Maze myMaze){
        Collections.replaceAll(myMaze.getMaze(),'.', ' ');
        myMaze.setMazeView(myMaze.getMaze());
    }
}