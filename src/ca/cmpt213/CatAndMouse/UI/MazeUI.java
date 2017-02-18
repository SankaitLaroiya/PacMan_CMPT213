package ca.cmpt213.CatAndMouse.UI;

import java.util.ArrayList;

/**
 * Class to handle user interaction (input and output)
 */
public class MazeUI {

    public static void printMaze(ArrayList<?> maze) {
        for(int x = 0; x < maze.size(); x++) {
            if(x % 20 == 0) {
                printToScr("\n");
            }
            printToScr(maze.get(x));
        }
        printToScr("\n");
    }

    public static <E> void printToScr(E output) {
        System.out.print(output);
    }

    public static void showGameMenu(boolean initialLaunch) {
        if(initialLaunch) {
            printToScr("\n------------------------------\n");
            printToScr("Welcome to Chase the Cheese!\nBy Sankait Kumar & Jimmy Tran");
            printToScr("\n------------------------------\n");
        }

        printToScr("DIRECTIONS: \n");
        printToScr("\tFind 5 cheese before a cat eats you!\n");
        printToScr("LEGEND: \n");
        printToScr("\t#: Wall\n" +
                "\t@: You (a mouse)\n" +
                "\t!: Cat\n" +
                "\t$: Cheese\n" +
                "\t.: Unexplored space\n");

        printToScr("MOVES:\n");
        printToScr("\tUse W (up), A (left), S (down) and D (right) to move.\n" +
                "\t(You must press enter after each move).\n" +
                "\tUse E to exit.\n");

    }
}

