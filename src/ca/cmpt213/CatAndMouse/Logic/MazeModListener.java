package ca.cmpt213.CatAndMouse.Logic;

/**
 * Interface detailing the methods required for being a Maze listener.
 */
public interface MazeModListener {

    /**
     * Called whenever a position or whole maze is modified.
     */
    void mazeModified();

    /**
     * Called whenever stats (no. of cheese collected) are modified.
     */
    void statsModified();
}
