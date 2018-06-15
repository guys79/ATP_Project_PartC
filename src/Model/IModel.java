package Model;

import algorithms.search.Solution;
import javafx.scene.input.KeyCode;



/**
 * This interface represents a model
 */
public interface IModel {

    /**
     * This function will generate a new maze
     * @param width - The width of the maze
     * @param height - The height of the maze
     */
    public void generateMaze(int width, int height);

    /**
     * This function will move the character's position
     * The whereabouts of the character will be determined by the key that was pressed on the keyboard
     * @param movement - The key
     */
    public void moveCharacter(KeyCode movement);

    /**
     * This function will return the maze
     * @return - The maze
     * The maze is a two dimensional array that contains 0 and 1
     */
    public int[][] getMaze();

    /**
     * This function will return the row position of the character
     * @return - The row position of the character
     */
    public int getCharacterPositionRow();

    /**
     * This function will return the column position of the character
     * @return - The column position of the character
     */
    public int getCharacterPositionColumn();


    /**
     * This function will check whether the new move is a legal move or not
     * @param movement - The movement
     * @return
     */
    public boolean IsLegalMove(KeyCode movement);

    public Solution solveMaze();

    public Solution getSolution();

    public boolean lastChangeBecauseOfSolve();

    public void saveMaze(String path);
    public void loadMaze(String path);
    public boolean win();
    public void stopServers();
}
