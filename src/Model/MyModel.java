package Model;


import javafx.scene.input.KeyCode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Observable;

/**
 * This class will be our model (The maze).
 */
public class MyModel extends Observable implements IModel {
    /**
     * This function will generate a new maze
     * @param width - The width of the maze
     * @param height - The height of the maze
     */
    public void generateMaze(int width, int height)
    {
        throw new NotImplementedException();
    }

    /**
     * This function will move the character's position
     * The whereabouts of the character will be determined by the key that was pressed on the keyboard
     * @param movement - The key
     */
    public void moveCharacter(KeyCode movement)
    {
        throw new NotImplementedException();
    }

    /**
     * This function will return the maze
     * @return - The maze
     * The maze is a two dimensional array that contains 0 and 1
     */
    public int[][] getMaze()
    {
        throw new NotImplementedException();
    }

    /**
     * This function will return the row position of the character
     * @return - The row position of the character
     */
    public int getCharacterPositionRow()
    {
        throw new NotImplementedException();
    }

    /**
     * This function will return the column position of the character
     * @return - The column position of the character
     */
    public int getCharacterPositionColumn()
    {
        throw new NotImplementedException();
    }

    /**
     * This function will check whether the new move is a legal move or not
     * @param movement - The movement
     * @return
     */
    public boolean IsLegalMove(KeyCode movement)
    {
        throw new NotImplementedException();
    }
}
