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
    private static ArrayList<Integer> mazeCorners;
    private static ArrayList<MazeModListener> listeners = new ArrayList<>(1);

    private static Integer mazeWidth;
    private static Integer mazeHeight;
    private static Integer mazeArea;

    private static int numCheeseCollected = 0;

    public Maze(int height, int width) {
        mazeHeight = height;
        mazeWidth = width;

        mazeArea = mazeHeight * mazeWidth;

        maze = new ArrayList<Character>(mazeArea);
        mazeView = new ArrayList<Character>(mazeArea);
        mazeCorners = new ArrayList<>(4);

        //Populate the maze and maze view
        for(int x = 0; x < mazeArea; x++) {
            maze.add(x, '.');
            mazeView.add(x, ' ');
        }

        mazeWallPositions = new ArrayList<Integer>(120);

        calculateMazeCorners();



        notifyMazeChange();
    }

    public int getNumCheeseCollected() {
        return numCheeseCollected;
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

    public ArrayList<Integer> getMazeCorners() {
        return mazeCorners;
    }

    public Integer getMazeWidth() {
        return mazeWidth;
    }

    public Integer getMazeHeight() { return mazeHeight; }

    public void setMazeView(ArrayList<Character> newMazeView) {
        mazeView = newMazeView;
        notifyMazeChange();
    }

    /**
     * Method to populate the maze with basic walls to make a grid.
     */
    public void constructMaze() {
        // Why does the method name sounds familiar? :P

        for(int x = 0; x < maze.size(); x++) {

            if((x % mazeWidth == 0 || x % mazeWidth == mazeWidth - 1)
                    || (x >= 0 && x <= mazeWidth)
                    || (x >= (mazeArea - mazeWidth + 1) && x <= mazeArea)) {
                maze.set(x, '#');
                mazeWallPositions.add(x);
            }
        }

        //Save the basic maze for UI.
        Collections.copy(mazeView, maze);

        //Horizontal grid walls
        for(int x = 0; x < maze.size(); x += mazeWidth) {


            if((x % 3) * mazeWidth == 0){
                for(int y = x; y < x + mazeWidth; y++) {
                    //skips placing first wall segment in the first wall group below the player.
                    if(y == (mazeCorners.get(0) + (2 * mazeWidth))) {
                        continue;
                    }
                    maze.set(y, '#');
                    mazeWallPositions.add(y);
                }
            }
        }

        //Vertical Grid walls
        for(int x = 0; x < mazeWidth; x+= 4) {
            for(int y = x; y < mazeArea; y += mazeWidth) {
                maze.set(y, '#');
                if(!(mazeWallPositions.contains(y))) {
                    mazeWallPositions.add(y);
                }
            }
        }

        //The same statement can be executed multiple times to get the desired density of the walls.
        //The more times its executed on the maze, the less denser it will become.
        applyPrimsAlgorithm();

        Collections.replaceAll(maze,'.', ' ');
    }

    public void modifyMazePos(int pos, Character withChar) {
        maze.set(pos, withChar);
    }

    public void modifyMazeViewAtPos(int x, Character withChar) {
        mazeView.set(x, withChar);
        notifyMazeChange();
    }

    public void revealFog(int playerPos) {
        mazeView.set(playerPos + 1, maze.get(playerPos + 1));
        mazeView.set(playerPos - 1, maze.get(playerPos - 1));
        mazeView.set(playerPos + mazeWidth, maze.get(playerPos + mazeWidth));
        mazeView.set(playerPos - mazeWidth, maze.get(playerPos - mazeWidth));
        notifyMazeChange();
    }

    public void addMazeModListener(MazeModListener newListener) {
        listeners.add(newListener);
    }

    /**
     * Method to analyze where the corners (excluding the perimeter walls) lie within the maze.
     */
    private void calculateMazeCorners() {
        Integer upperLeft = mazeWidth + 1;
        Integer upperRight = ((mazeWidth - 1) * 2);
        Integer lowerRight = (mazeArea - 1) - mazeWidth - 1;
        Integer lowerLeft = lowerRight - mazeWidth + 3;

        mazeCorners.add(0, upperLeft);
        mazeCorners.add(1, upperRight);
        mazeCorners.add(2, lowerLeft);
        mazeCorners.add(3, lowerRight);
    }

    /**
     * Applies Prim's Algorithm to the basic maze.
     * Reference: https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Prim.27s_algorithm
     */
    private void applyPrimsAlgorithm() {
        Random remove = new Random();

        //Cannot delete an edge while iterating over an array of edges,
        //so, need to store the edges to be removed after the loop.
        ArrayList<Integer> removeLater = new ArrayList<Integer>();

        for(Integer x : mazeWallPositions) {

            //Avoid perimeter walls
            if ((x % mazeWidth == 0
                    || x % mazeWidth == (mazeWidth - 1))
                    || (x >= 0 && x <= mazeWidth -1)
                    || (x >=(mazeWidth * (mazeHeight - 1) + 1) && x <= (mazeHeight * mazeWidth))) {

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

    public void notifyMazeChange() {
        for(MazeModListener m : listeners) {
            m.mazeModified();
        }
    }

    public void incrementNumCheese() {
        numCheeseCollected++;
        notifyStatChange();
    }

    private void notifyStatChange() {
        for(MazeModListener m : listeners) {
            m.statsModified();
        }
    }
}