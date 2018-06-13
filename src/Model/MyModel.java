package Model;

import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Observable;

/**
 * This class will be our model (The maze).
 */
public class MyModel extends Observable implements IModel {
    private Maze maze;//The maze
    private Server mazeGenerator=new Server(5400,1000,new ServerStrategyGenerateMaze());// Maze generator server
    private Server mazeSolver=new Server(5401,1000,new ServerStrategySolveSearchProblem());// Maze solver server
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
    public void moveCharacter(KeyCode movement) {

        int tempRow=characterPositionRow;
        int tempColumn=characterPositionColumn;

        switch (movement) {
            //Up
            case NUMPAD8:
                tempRow--;
                break;
            //Down
            case NUMPAD2:
                tempRow++;
                break;
            //Right
            case NUMPAD6:
                tempColumn++;
                break;
            //Left
            case NUMPAD4:
                tempColumn--;
                break;
            //Right + Down
            case NUMPAD3:
                tempColumn++;
                tempRow++;
                break;
            //Left + Down
            case NUMPAD1:
                tempColumn--;
                tempRow++;
                break;
            //Left + Up
            case NUMPAD7:
                tempColumn--;
                tempRow--;
                break;
            //Right + Up
            case NUMPAD9:
                tempColumn++;
                tempRow--;
                break;
        }
        if(isLegalMoveMazeCheck(tempRow,tempColumn))
        {
            characterPositionRow=tempRow;
            characterPositionColumn=tempColumn;
            setChanged();
            notifyObservers();
        }

    }
    public boolean isLegalMoveMazeCheck(int row,int col)
    {
        if(this.maze==null)
            return false;


        return !(row<0||row==maze.length|| col<0||col==maze[0].length||maze[row][col]==1);
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
