package ca.cmpt213.CatAndMouse.Logic;

import ca.cmpt213.CatAndMouse.UI.MazeGame;
import ca.cmpt213.CatAndMouse.UI.MazeUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to move the cats every click in the game and handle player moves.
 */
public class MazeActorController {
    private static Integer cat1Pos;
    private static Integer cat2Pos;
    private static Integer cat3Pos;
    public static Integer playerPos;
    private static Integer cheesePos;

    public static void initGameActors(Maze gameMaze) {

        cat1Pos = MazeGame.CAT1_POS;
        cat2Pos = MazeGame.CAT2_POS;
        cat3Pos = MazeGame.CAT3_POS;
        playerPos = MazeGame.PLAYER_POS;

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

    /**
     * Method to check that all the actors have at least one direction to move into after initial placing.
     * @param gameMaze The maze the actors are on.
     */
    private static void checkActorsMobility(Maze gameMaze) {
        ArrayList<Character> mazeView = gameMaze.getMazeView();
        ArrayList<Integer> mazeEdges = gameMaze.getMazeWallPositions();

        //Ensures that each actor has at least one direction to move towards initially.
        //The if statements use the array which maintains the positions of the internal walls of the maze
        //to make sure that there is atleast one way out for each actor.
        if((mazeEdges.contains(playerPos + 1))
                && (mazeEdges.contains(playerPos + 20))) {

            gameMaze.modifyMazePos(playerPos + 20, '.');
            mazeEdges.remove(new Integer(playerPos + 20));
        }

        if((mazeEdges.contains(cat1Pos - 1))
                && (mazeEdges.contains(cat1Pos + 20))) {

            gameMaze.modifyMazePos(cat1Pos - 1, '.');
            mazeEdges.remove(new Integer(cat1Pos - 1));
        }

        if((mazeEdges.contains(cat2Pos + 1))
                && (mazeEdges.contains(cat2Pos - 20))) {

            gameMaze.modifyMazePos(cat2Pos - 20, '.');
            mazeEdges.remove(new Integer(cat2Pos - 20));
        }

        if((mazeEdges.contains(cat3Pos - 1))
                && (mazeEdges.contains(cat3Pos - 20))) {

            gameMaze.modifyMazePos(cat3Pos - 20, '.');
            mazeEdges.remove(new Integer(cat3Pos - 20));
        }

        //Places cheese until it is accessible from at east one direction.
        while((mazeEdges.contains(cheesePos + 20))
                && (mazeEdges.contains((cheesePos - 20)))
                && (mazeEdges.contains(cheesePos - 1))
                && (mazeEdges.contains(cheesePos + 1))) {

            gameMaze.modifyMazePos(cheesePos, '.');
            mazeView.set(cheesePos, '.');
            mazeEdges.remove(cheesePos);

            placeCheese(gameMaze);
        }
    }

    // REMEMBER: x = new move + player position
    public static void movePlayer(int x, Maze gameMaze) {

        ArrayList<Character> maze = gameMaze.getMaze();
        ArrayList<Character> mazeView = gameMaze.getMazeView();
        ArrayList<Integer> mazeEdges = gameMaze.getMazeWallPositions();

        //Check if the selected move is valid
        //TODO: MOVE PRINTTOSCR FUNCTION OUT OF THIS CLASS
        if(mazeEdges.contains(x)) {
            MazeUI.printToScr("Invalid Move: You cannot move through walls!\n");
            return;
        }

        //If valid move the player
        maze.set(playerPos, ' ');
        mazeView.set(playerPos, ' ');

        maze.set(x, '@');
        mazeView.set(x, '@');

        playerPos = x;

        //TODO: ADD IF PLAYER IS MOVING TOWARDS A CAT OR CHEESE.
        //Check if the player got a cheese or was caught by a cat

        moveCats(gameMaze);
    }

    private static void placeCheese(Maze gameMaze) {
        ArrayList<Integer> tempMazeEdge = new ArrayList<>(gameMaze.getMazeWallPositions());
        ArrayList<Character> mazeView = gameMaze.getMazeView();

        Collections.shuffle(tempMazeEdge);
        Integer x = tempMazeEdge.get(0);

        while(true) {
            //Ensures that the perimeter walls or the actor's locations
            //are not replaced with cheese.
            //Only the inside walls are considered.
            if((x % 20 == 0 || x % 20 == 19)
                    || (x >= 0 && x <= 19)
                    || (x >= 279 && x <= 300) ||(x == playerPos)
                    || (x == cat1Pos)
                    || (x == cat2Pos)
                    || (x == cat3Pos)) {
                Collections.shuffle(tempMazeEdge);
                x = tempMazeEdge.get(0);


            }
            else {
                break;
            }
        }

        gameMaze.modifyMazePos(x, '$');
        gameMaze.getMazeWallPositions().remove(x);

        mazeView.set(x, '$');

        cheesePos = x;
    }

    static void moveCats(Maze gameMaze) {
        ArrayList<Character> maze = gameMaze.getMaze();
        ArrayList<Character> mazeView = gameMaze.getMazeView();

        ArrayList<Integer> cats = new ArrayList<Integer>(3);

        cats.add(cat1Pos);
        cats.add(cat2Pos);
        cats.add(cat3Pos);

        ArrayList directions = new ArrayList<Integer>(4);
        directions.add(-20);
        directions.add(20);
        directions.add(-1);
        directions.add(1);

        Collections.shuffle(directions);

        Integer stepTowards = (Integer)directions.get(0);
        stepTowards += cat1Pos;

        gameMaze.modifyMazePos(cat1Pos, '.');
        gameMaze.modifyMazePos(stepTowards, '!');

        gameMaze.modifyMazeViewAtPos(cat1Pos, '.');
        gameMaze.modifyMazeViewAtPos(stepTowards, '!');

        cat1Pos = stepTowards;

    }
}
