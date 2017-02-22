package ca.cmpt213.CatAndMouse.Logic;

import ca.cmpt213.CatAndMouse.UI.MazeGame;
import ca.cmpt213.CatAndMouse.UI.MazeUI;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to move the cats every click in the game and handle player moves.
 */
public class MazeActorController {
    public static int playerPos;
    private static int cheesePos;
    private static ArrayList<Integer> catPositions = new ArrayList<>(3);
    private static ArrayList<Character> catPrevStepLog = new ArrayList<>(3);


    public static void initGameActors(Maze gameMaze) {

        ArrayList<Integer> corners = gameMaze.getMazeCorners();

        catPrevStepLog.add(' ');
        catPrevStepLog.add(' ');
        catPrevStepLog.add(' ');

        playerPos = corners.get(0);

        Integer cat1Pos = corners.get(1);
        Integer cat2Pos = corners.get(2);
        Integer cat3Pos = corners.get(3);

        catPositions.add(0, cat1Pos);
        catPositions.add(1, cat2Pos);
        catPositions.add(2, cat3Pos);

        ArrayList<Character> mazeView = gameMaze.getMazeView();

        gameMaze.modifyMazePos(playerPos, '@');
        mazeView.set(playerPos, '@');

        gameMaze.modifyMazePos(cat1Pos, '!');
        mazeView.set(cat1Pos, '!');

        gameMaze.modifyMazePos(cat2Pos, '!');
        mazeView.set(cat2Pos, '!');

        gameMaze.modifyMazePos(cat3Pos, '!');
        mazeView.set(cat3Pos, '!');

        placeCheese(gameMaze);

        checkActorsMobility(gameMaze);
    }

    // NOTE: x = new move + player position
    public static void movePlayer(int x, Maze gameMaze) {
        ArrayList<Character> maze = gameMaze.getMaze();
        ArrayList<Character> mazeView = gameMaze.getMazeView();
        ArrayList<Integer> mazeEdges = gameMaze.getMazeWallPositions();

        //Check if the selected move is valid
        if (mazeEdges.contains(x)) {
            MazeUI.printToScr("Invalid Move: You cannot move through walls!\n");
            return;
        }

        maze.set(x, '@');
        mazeView.set(x, '@');
        maze.set(playerPos, ' ');
        mazeView.set(playerPos, ' ');

        playerPos = x;

        checkCondition(gameMaze);
    }

    public static void moveCats(Maze gameMaze) {
        Integer moveToPos;
        Character catPrevStep;

        ArrayList maze = gameMaze.getMaze();
        ArrayList mazeView = gameMaze.getMazeView();
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
    }

    public static void checkCondition(Maze gameMaze) {

        if (playerPos == catPositions.get(0) || playerPos == catPositions.get(1) || playerPos == catPositions.get(2)) {
            gameMaze.modifyMazePos(playerPos, 'X');
            gameMaze.modifyMazeViewAtPos(playerPos, 'X');
            MazeGame.gameLost = true;

        } else if (playerPos == cheesePos) {
            placeCheese(gameMaze);
            MazeGame.numCheeseCollected++;
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
        Integer cat2Pos = catPositions.get(1);
        Integer cat3Pos = catPositions.get(2);

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

        if ((mazeEdges.contains(cat2Pos + 1))
                && (mazeEdges.contains(cat2Pos - mazeWidth))) {

            gameMaze.modifyMazePos(cat2Pos - mazeWidth, '.');
            mazeEdges.remove(new Integer(cat2Pos - mazeWidth));
        }

        if ((mazeEdges.contains(cat3Pos - 1))
                && (mazeEdges.contains(cat3Pos - mazeWidth))) {

            gameMaze.modifyMazePos(cat3Pos - mazeWidth, '.');
            mazeEdges.remove(new Integer(cat3Pos - mazeWidth));
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
            //mazeEdges.remove(cheesePos);

            placeCheese(gameMaze);
        }
    }

    private static void placeCheese(Maze gameMaze) {
        ArrayList<Integer> tempMazeEdge = new ArrayList<>(gameMaze.getMazeWallPositions());
        ArrayList<Character> mazeView = gameMaze.getMazeView();

        Integer mazeWidth = gameMaze.getMazeWidth();
        Integer mazeHeight = gameMaze.getMazeHeight();

        Integer cat1Pos = catPositions.get(0);
        Integer cat2Pos = catPositions.get(1);
        Integer cat3Pos = catPositions.get(2);

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
                    || (x == cat3Pos)) {

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

    public static void resetCatSteps() {
        Collections.replaceAll(catPrevStepLog,'.', ' ');
    }
}