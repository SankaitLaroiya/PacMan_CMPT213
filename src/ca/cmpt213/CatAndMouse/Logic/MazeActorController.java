package ca.cmpt213.CatAndMouse.Logic;

import java.util.ArrayList;
import java.util.Collections;

import ca.cmpt213.CatAndMouse.UI.MazeGUI;

import static ca.cmpt213.CatAndMouse.UI.MazeGUI.*;
import static ca.cmpt213.CatAndMouse.UI.MazeGame.gameLost;
import static ca.cmpt213.CatAndMouse.UI.MazeGame.uncoverMaze;

/**
 * Class to implement player moves, move cats every click of the game and place cheese on the game board.
 */
public class MazeActorController {
    public static int playerPos;
    private static int cheesePos;

    private static ArrayList<Integer> catPositions = new ArrayList<>(3);
    private static ArrayList<Character> catPrevStepLog = new ArrayList<>(3);

    public MazeActorController() {

    }

    public static void initGameActors(Maze gameMaze) {

        ArrayList<Integer> corners = gameMaze.getMazeCorners();

        catPrevStepLog.add('.');
        catPrevStepLog.add('.');
        catPrevStepLog.add('.');
        catPrevStepLog.add('.');
        catPrevStepLog.add('.');
        catPrevStepLog.add('.');

        playerPos = corners.get(0);

        Integer cat1Pos = corners.get(1);
        Integer cat1APos = cat1Pos - 1;

        Integer cat2Pos = corners.get(2);
        Integer cat2APos = cat2Pos + 1;

        Integer cat3Pos = corners.get(3);
        Integer cat3APos = cat3Pos - 1;

        catPositions.add(0, cat1Pos);
        catPositions.add(1, cat1APos);

        catPositions.add(2, cat2Pos);
        catPositions.add(3, cat2APos);

        catPositions.add(4, cat3Pos);
        catPositions.add(5, cat3APos);


        ArrayList<Character> mazeView = gameMaze.getMazeView();

        mazeView.set(playerPos, '@');
        gameMaze.modifyMazePos(playerPos, '@');

        mazeView.set(cat1Pos, '!');
        gameMaze.modifyMazePos(cat1Pos, '!');

        mazeView.set(cat1APos, '!');
        gameMaze.modifyMazePos(cat1Pos, '!');

        mazeView.set(cat2Pos, '!');
        gameMaze.modifyMazePos(cat2Pos, '!');

        mazeView.set(cat2APos, '!');
        gameMaze.modifyMazePos(cat1Pos, '!');

        mazeView.set(cat3Pos, '!');
        gameMaze.modifyMazePos(cat3Pos, '!');

        mazeView.set(cat3APos, '!');
        gameMaze.modifyMazePos(cat1Pos, '!');

        placeCheese(gameMaze);

        checkActorsMobility(gameMaze);
    }

    public static void movePlayer(int x, Maze gameMaze) {
        x = x + playerPos;

        ArrayList<Character> maze = gameMaze.getMaze();
        ArrayList<Character> mazeView = gameMaze.getMazeView();
        ArrayList<Integer> mazeEdges = gameMaze.getMazeWallPositions();

        //Check if the selected move is valid
        if (mazeEdges.contains(x)) {
            MazeGUI.playSound(INVALID_MOVE);
            return;
        }

        gameMaze.modifyMazeViewAtPos(x, '@');
        gameMaze.modifyMazePos(x, '@');

        //mazeView.set(x, '@');
        //maze.set(x, '@');

        gameMaze.modifyMazeViewAtPos(playerPos, ' ');
        gameMaze.modifyMazePos(playerPos, ' ');

        //mazeView.set(playerPos, ' ');
        //maze.set(playerPos, ' ');

        playerPos = x;

        gameMaze.revealFog(playerPos);

        MazeGUI.playSound(PLAYER_MOVE);

        checkCondition(gameMaze);
    }

    public static void moveCats(Maze gameMaze) {
        Integer moveToPos;
        Character catPrevStep;

        ArrayList maze = gameMaze.getMaze();
        ArrayList mazeEdges = gameMaze.getMazeWallPositions();

        ArrayList directions = new ArrayList<Integer>(4);

        Integer mazeWidth = gameMaze.getMazeWidth();

        directions.add((-1 * mazeWidth));
        directions.add(mazeWidth);
        directions.add(1);
        directions.add(-1);


        for(int x = 0; x < catPositions.size(); x++) {
            int catPos = catPositions.get(x);

            Collections.shuffle(directions);
            moveToPos = (Integer) directions.get(0);

            while (true) {

                moveToPos += catPos;

                if (mazeEdges.contains(moveToPos) || moveToPos > maze.size()
                        || moveToPos < 0) {
                    Collections.shuffle(directions);
                    moveToPos = (Integer) directions.get(0);

                } else {
                    break;
                }
            }

            char replacementStep = catPrevStepLog.get(x);

            catPrevStep = (Character)maze.get(moveToPos);

            if(catPrevStep == '!'|| catPrevStep == '@') {
                catPrevStepLog.set(x, ' ');
            }
            else {
                catPrevStepLog.set(x, catPrevStep);
            }

            gameMaze.modifyMazePos(catPos, replacementStep);
            gameMaze.modifyMazePos(moveToPos, '!');

            gameMaze.modifyMazeViewAtPos(catPos, replacementStep);
            gameMaze.modifyMazeViewAtPos(moveToPos, '!');

            catPositions.set(x, moveToPos);
        }

        MazeGUI.playSound(CAT_MOVE);

        checkCondition(gameMaze);
    }

    public static void resetCatSteps() {
        Collections.replaceAll(catPrevStepLog,'.', ' ');
    }

    private static void checkCondition(Maze gameMaze) {

        for(Integer catPos : catPositions) {
            if(playerPos == catPos) {
                gameMaze.modifyMazePos(playerPos, 'X');
                gameMaze.modifyMazeViewAtPos(playerPos, 'X');
                gameLost = true;
            }
        }

        if (playerPos == cheesePos) {
            placeCheese(gameMaze);
            gameMaze.incrementNumCheese();
            MazeGUI.playSound(CHEESE_COLLECTED);
        }

        if(gameMaze.getNumCheeseCollected() >= 5) {
            uncoverMaze(gameMaze);
            MazeGUI.playSound(GAME_WON);
            MazeGUI.infoBox("You Won!", "Game Over");
        }

        if(gameLost){
            uncoverMaze(gameMaze);
            MazeGUI.playSound(GAME_LOST);
            MazeGUI.infoBox("You Lost", "Game Over");
        }

    }

    /**
     * Method to check that all the actors have at least one direction to move into after initial placing.
     *
     * @param gameMaze The maze the actors are on.
     */
    private static void checkActorsMobility(Maze gameMaze) {
        ArrayList<Integer> mazeEdges = gameMaze.getMazeWallPositions();

        Integer mazeWidth = gameMaze.getMazeWidth();

        Integer cat1Pos = catPositions.get(0);
        Integer cat1APos = catPositions.get(1);

        Integer cat2Pos = catPositions.get(2);
        Integer cat2APos = catPositions.get(3);

        Integer cat3Pos = catPositions.get(4);
        Integer cat3APos = catPositions.get(5);


        //Ensures that each actor has at least one direction to move towards initially.
        //The if statements use the array which maintains the positions of the internal walls of the maze
        //to make sure that there is atleast one way out for each actor.
        if ((mazeEdges.contains(playerPos + 1))
                && (mazeEdges.contains(playerPos + mazeWidth))) {

            gameMaze.modifyMazePos(playerPos + mazeWidth, '.');
            mazeEdges.remove(new Integer(playerPos + mazeWidth));
        }

        if ((mazeEdges.contains(cat1Pos - 1))
                && (mazeEdges.contains(cat1Pos + mazeWidth))) {

            gameMaze.modifyMazePos(cat1Pos - 1, '.');
            mazeEdges.remove(new Integer(cat1Pos - 1));
        }

        if ((mazeEdges.contains(cat1APos - 1))
                && (mazeEdges.contains(cat1APos + mazeWidth))) {

            gameMaze.modifyMazePos(cat1APos - 1, '.');
            mazeEdges.remove(new Integer(cat1APos - 1));
        }

        if ((mazeEdges.contains(cat2Pos + 1))
                && (mazeEdges.contains(cat2Pos - mazeWidth))) {

            gameMaze.modifyMazePos(cat2Pos - mazeWidth, '.');
            mazeEdges.remove(new Integer(cat2Pos - mazeWidth));
        }

        if ((mazeEdges.contains(cat2APos + 1))
                && (mazeEdges.contains(cat2APos - mazeWidth))) {

            gameMaze.modifyMazePos(cat2APos - mazeWidth, '.');
            mazeEdges.remove(new Integer(cat2APos - mazeWidth));
        }

        if ((mazeEdges.contains(cat3Pos - 1))
                && (mazeEdges.contains(cat3Pos - mazeWidth))) {

            gameMaze.modifyMazePos(cat3Pos - mazeWidth, '.');
            mazeEdges.remove(new Integer(cat3Pos - mazeWidth));
        }

        if ((mazeEdges.contains(cat3APos - 1))
                && (mazeEdges.contains(cat3APos - mazeWidth))) {

            gameMaze.modifyMazePos(cat3APos - mazeWidth, '.');
            mazeEdges.remove(new Integer(cat3APos - mazeWidth));
        }

        checkCheeseAccess(gameMaze);
    }

    private static void checkCheeseAccess(Maze gameMaze) {
        ArrayList<Character> mazeView = gameMaze.getMazeView();
        ArrayList<Integer> mazeEdges = gameMaze.getMazeWallPositions();
        Integer mazeWidth = gameMaze.getMazeWidth();

        //Places cheese until it is accessible from at east one direction.
        while ((mazeEdges.contains(cheesePos + mazeWidth))
                && (mazeEdges.contains((cheesePos - mazeWidth)))
                && (mazeEdges.contains(cheesePos - 1))
                && (mazeEdges.contains(cheesePos + 1))) {

            gameMaze.modifyMazePos(cheesePos, '.');
            mazeView.set(cheesePos, '.');

            placeCheese(gameMaze);
        }
    }

    private static void placeCheese(Maze gameMaze) {
        ArrayList<Integer> tempMazeEdge = new ArrayList<>(gameMaze.getMazeWallPositions());
        ArrayList<Character> mazeView = gameMaze.getMazeView();

        Integer mazeWidth = gameMaze.getMazeWidth();
        Integer mazeHeight = gameMaze.getMazeHeight();

        Integer cat1Pos = catPositions.get(0);
        Integer cat1APos = catPositions.get(1);

        Integer cat2Pos = catPositions.get(2);
        Integer cat2APos = catPositions.get(3);

        Integer cat3Pos = catPositions.get(4);
        Integer cat3APos = catPositions.get(5);

        Collections.shuffle(tempMazeEdge);
        Integer x = tempMazeEdge.get(0);

        while (true) {
            //Ensures that the perimeter walls or the actor's locations
            //are not replaced with cheese.
            //Only the inside walls are considered.
            if ((x % mazeWidth == 0
                    || x % mazeWidth == (mazeWidth - 1))
                    || (x >= 0 && x <= mazeWidth -1)
                    || (x >=(mazeWidth * (mazeHeight - 1) + 1) && x <= (mazeHeight * mazeWidth))
                    || (x == playerPos)
                    || (x == cat1Pos)
                    || (x == cat2Pos)
                    || (x == cat3Pos)
                    || (x == cat1APos)
                    || (x == cat2APos)
                    || (x == cat3APos)) {

                Collections.shuffle(tempMazeEdge);
                x = tempMazeEdge.get(0);

            } else {
                break;
            }
        }

        gameMaze.modifyMazePos(x, '$');
        gameMaze.modifyMazeViewAtPos(x, '$');
        gameMaze.getMazeWallPositions().remove(x);
        mazeView.set(x, '$');
        cheesePos = x;

        checkCheeseAccess(gameMaze);
    }
}