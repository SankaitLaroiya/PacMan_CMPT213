package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;

import java.awt.*;
import java.util.Collections;

import static ca.cmpt213.CatAndMouse.Logic.MazeActorController.playerPos;
import static ca.cmpt213.CatAndMouse.UI.MazeTerminalUI.printMaze;
import static ca.cmpt213.CatAndMouse.UI.MazeTerminalUI.showGameMenu;

import javax.swing.*;

/**
 * Cat and Mouse game's main activity class. Co-ordinates user input with other classes.
 * Git Repository @: https://csil-git1.cs.surrey.sfu.ca/sankaitk/CMPT213-ChaseTheCheese.git
 */
public class MazeGame {
    public static boolean gameLost = false;
    public static int numCheeseCollected = 0;

    public static void main(String[] args) {
        //The assignment spec wants the maze to be only 20 x 15 however, the values of
        //height and width below can be changed to get a game with the desired maze size.
        //for a proper looking maze, please use size height = 20, width = 35 to see the
        //maze algorithm in action
        int height = 15;
        int width = 20;

        Maze gameMaze = new Maze(height, width);

        gameMaze.constructMaze();
        MazeActorController.initGameActors(gameMaze);

        showGameMenu(true);

        gameMaze.revealFog(playerPos);

        JFrame frame = new JFrame();                            // Make the frame
        frame.setLayout(new GridBagLayout());                      // How items fit in frame.
        frame.add(new MazeInputController(gameMaze));


        //TODO: FIGURE OUT HOW TO ADD ELEMENTS IN THE GRID BAG LAYOUT.

        frame.pack();                                           // Resize to fit contents.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // When frame closes, end program.
        frame.setVisible(true);


        printMaze(gameMaze.getMazeView(), width);
    }

    public static void uncoverMaze(Maze myMaze){
        Collections.replaceAll(myMaze.getMaze(),'.', ' ');
        myMaze.setMazeView(myMaze.getMaze());
        MazeActorController.resetCatSteps();
    }
}