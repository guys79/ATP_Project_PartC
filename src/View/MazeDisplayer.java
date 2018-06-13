package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class will display a maze
 */
public class MazeDisplayer extends Canvas {

    private int[][] maze;//The maze
    private int characterPositionRow = 1;//The row position of the character
    private int characterPositionColumn = 1;//The column position of the character

    //The source of the image
    //A default value is defined in the fxml
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    /**
     * This function will set the maze
     * @param maze  -The given maze
     */
    public void setMaze(int[][] maze) {

        this.maze = maze;
        //Draw the maze with the new data
        redraw();
    }


    /**
     * This function will set the character's position
     * @param row - The row position
     * @param column - The col position
     */
    public void setCharacterPosition(int row, int column) {
        //The character's position
        characterPositionRow = row;
        characterPositionColumn = column;
        //Draw the maze with the new data
        redraw();
    }

    /**
     * This function will return the character's row position
     * @return - The character's row position
     */
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    /**
     * This function will return the character's column position
     * @return - The character's column position
     */
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    /**
     * This function is responsible to draw the maze with a give data (The fiels of this class)
     */
    public void redraw() {
        //If there is a maze
        if (maze != null) {
            //Get the size of the canvas
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();

            //Calculate the siz of each  cell in the maze
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                //Getting the images
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

                //Create the maze structure
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw the maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                        }
                    }
                }

                //Draw the character
                gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
            } catch (FileNotFoundException e) {
                /**print alert?*/
            }
        }
    }



    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
    //endregion

}
