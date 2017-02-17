package ca.cmpt213.CatAndMouse.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class to create, modify and manage the maze
 */
public class Maze {
    private static ArrayList<Character> maze;
    private static ArrayList<Integer> mazeWallPositions;
    private static ArrayList<Character> mazeView;

    public Maze() {
        maze = new ArrayList<Character>(300);
        mazeView = new ArrayList<Character>(300);

        //Populate the maze and maze view
        for(int x = 0; x < 300; x++) {
            maze.add(x, '.');
            mazeView.add(x, ' ');
        }

        mazeWallPositions = new ArrayList<Integer>(120);
    }

    public ArrayList<Character> getMaze() {
        return maze;
    }

    public ArrayList<Character> getMazeView() {
        return mazeView;
    }

    public ArrayList<Integer> getMazeWallPositions() {
        return mazeWallPositions;
    }

    public void setMazeView(ArrayList<Character> newMazeView) {
        mazeView = newMazeView;
    }

    /**
     * Method to populate the maze with basic walls to make a grid.
     */
    public void constructMaze() {
        // Why does the method name sounds familiar? :P

        for(int x = 0; x < maze.size(); x++) {

            if((x % 20 == 0 || x % 20 == 19)
                    || (x >= 0 && x <= 19)
                    || (x >= 279 && x <= 300)) {
                maze.set(x, '#');
                mazeWallPositions.add(x);
            }
        }

        //Save the basic maze for UI.
        Collections.copy(mazeView, maze);

        for(int x = 0; x < maze.size(); x+=20) {
            if(x % 40 == 0){
                for(int y = x; y < x + 20; y++) {
                    maze.set(y, '#');
                    mazeWallPositions.add(y);
                }
            }
        }
        for(int x = 2; x < 18; x+= 2) {
            for(int y = x; y < 300; y+=20) {
                maze.set(y, '#');
                if(!(mazeWallPositions.contains(y))) {
                    mazeWallPositions.add(y);
                }
            }
        }

        applyKruskalAlgo();
        applyKruskalAlgo();
    }

    public void modifyMazePos(int pos, Character withChar) {
        maze.set(pos, withChar);
    }

    public void revealFog(int playerPos) {
        mazeView.set(playerPos + 1, maze.get(playerPos + 1));
        mazeView.set(playerPos - 1, maze.get(playerPos - 1));
        mazeView.set(playerPos + 20, maze.get(playerPos + 20));
        mazeView.set(playerPos - 20, maze.get(playerPos - 20));
    }

    private void applyKruskalAlgo() {
        Random remove = new Random();

        //Cannot delete an edge while iterating over an array of edges,
        //so, need to store the edges to be removed after the loop.
        ArrayList<Integer> removeLater = new ArrayList<Integer>();

        for(Integer x : mazeWallPositions) {

            //Avoid perimeter walls
            if((x % 20 == 0 || x % 20 == 19)
                    || (x >= 0 && x <= 19)
                    || (x >= 279 && x <= 300)) {
                continue;
            }

            //Randomly decide to remove the edge
            if(remove.nextBoolean()) {
                maze.set(x, '.');
                removeLater.add(x);
            }
        }

        //Invalidate fields which are no longer walls.
        for(Integer i : removeLater) {
            mazeWallPositions.remove(i);
        }
    }
}

