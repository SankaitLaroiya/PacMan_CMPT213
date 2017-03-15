package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeActorController;

import java.awt.*;
import java.util.Collections;

import static ca.cmpt213.CatAndMouse.Logic.MazeActorController.playerPos;

import javax.swing.*;

/**
 * Game's main activity class. Initializes the main game loop and other classes.
 * Git Repository @: https://csil-git1.cs.surrey.sfu.ca/sankaitk/CMPT213-ChaseTheCheese.git
 *
 * DISCLAIMER:
 * The sprites and sounds used in this game are properties of their respective owners.
 * They have been used only for the purpose of this assignment.
 *
 * Sources:
 * Sprites (Pac-man) - https://www.spriters-resource.com/arcade/pacman/sheet/52631/
 * Sound effects - https://www.sounds-resource.com/game_boy_advance/fireemblemtheblazingblade/sound/8833/
 *
 */
public class MazeGame {

    public static boolean gameLost = false;

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

        gameMaze.revealFog(playerPos);

        JFrame frame = new JFrame();                            // Make the frame

        frame.setLayout(new BorderLayout());                // How items fit in frame.

        MazeInputController inputController = new MazeInputController(gameMaze);
        inputController.setBackground(Color.BLACK);
        inputController.setLayout(new BorderLayout());

        JLabel instructions = new JLabel("Use arrow keys to move around.", SwingConstants.RIGHT);

        instructions.setForeground(Color.WHITE);

        inputController.add(instructions,
                BorderLayout.CENTER);

        frame.add(inputController, BorderLayout.NORTH);

        MazeGUI mazeGUI = new MazeGUI(frame, gameMaze);
        gameMaze.addMazeModListener(mazeGUI);

        frame.pack();                                           // Resize to fit contents.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // When frame closes, end program.
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void uncoverMaze(Maze myMaze){
        Collections.replaceAll(myMaze.getMaze(),'.', ' ');
        myMaze.setMazeView(myMaze.getMaze());
        MazeActorController.resetCatSteps();
    }
}