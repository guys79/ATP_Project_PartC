package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
;

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
    private StringProperty ImageFilePath = new SimpleStringProperty();
    private StringProperty ImageFileNameStart = new SimpleStringProperty();
    private StringProperty ImageFileNameEnd = new SimpleStringProperty();
    private StringProperty ImageFileNameSol = new SimpleStringProperty();
    private boolean isPathImageExist=true;



    /**
     * This function will set the maze
     *
     * @param maze -The given maze
     */
    public void setMaze(int[][] maze) {

        this.maze = maze;
        //Draw the maze with the new data
        redraw();
    }


    /**
     * This function will set the character's position
     *
     * @param row    - The row position
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
     *
     * @return - The character's row position
     */
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    /**
     * This function will return the character's column position
     *
     * @return - The character's column position
     */
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    private void nullHandle()
    {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();

        //Calculate the siz of each  cell in the maze
        double cellHeight = canvasHeight / maze.length;
        double cellWidth = canvasWidth / maze[0].length;
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        if (maze == null) {
            this.maze = new int[10][10];

        }
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if(i==j || i+j==this.maze.length-1)
                {
                    gc.setFill(Color.BLACK);
                }
                else
                {
                    gc.setFill(Color.YELLOW);
                }
                gc.fillRect(j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                this.maze[i][j] = 1;

            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("The Image of the wall/character does not exist");
        alert.show();
    }
    /**
     * This function is responsible to draw the maze with the given data (The fields of this class)
     */
    public void redraw() {

        if (ImageFileNameCharacter.getValue() == null || ImageFileNameWall.getValue() == null) {
            nullHandle();
        } else {
            if(ImageFileNameStart.getValue()==null) {

                ImageFileNameStart.setValue(ImageFilePath.getValue());
            }
            if(ImageFileNameEnd.getValue()==null)
            {

                ImageFileNameEnd.setValue(ImageFilePath.getValue());

            }
            //If there is a maze
            if (maze != null) {
                System.out.println("REDRAW STARTED");
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
                    Image end = new Image(new FileInputStream(ImageFileNameEnd.get()));

                    Image pathImage=null;
                    if(this.isPathImageExist)
                    {
                    pathImage = new Image(new FileInputStream(ImageFilePath.get()));}
                    System.out.println(ImageFilePath.get());

                    //Create the maze structure
                    GraphicsContext gc = getGraphicsContext2D();
                    gc.clearRect(0, 0, getWidth(), getHeight());


                    //Draw the maze
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[0].length; j++) {

                            if (maze[i][j] == 1) {
                                gc.drawImage(wallImage, j * cellHeight, i * cellWidth, cellHeight, cellWidth);

                            }
                            else
                            {
                                if(maze[i][j]==3)
                                {
                                    System.out.println("guyggg");
                                    gc.drawImage(end, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                                }
                                if(this.ImageFilePath.getValue()!=null && isPathImageExist) {
                                    System.out.println("??????????");
                                    gc.drawImage(pathImage, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                                }
                            }
                        }

                    }



                    System.out.println();

                    //Draw the character
                    gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                } catch (FileNotFoundException e) {
                    /**print alert?*/
                    e.printStackTrace();
                    System.out.println("??????????ss;dkd;ak;askda??");
                }
            }
        }
    }

    public void drawSolution(Solution sol) {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();

        //Calculate the size of each  cell in the maze
        double cellHeight = canvasHeight / maze.length;
        double cellWidth = canvasWidth / maze[0].length;
        int i = 1;
        if (this.getImageFileNameStart() == null || this.getImageFileNameStart().equals(this.getImageFilePath()))
            i = 0;
        int j = 1;
        if (this.getImageFileNameEnd() == null || this.getImageFileNameEnd().equals(this.getImageFilePath()))
            j = 0;
        ArrayList<AState> path = sol.getSolutionPath();
        GraphicsContext gc = getGraphicsContext2D();
        String imgSol = this.ImageFileNameSol.getValue();
        if (imgSol == null) {
            gc.setFill(Color.GOLD);
            for (; i < path.size() - j; i++) {
                gc.fillRect(((MazeState) path.get(i)).getCurrentPosition().GetRowIndex() * cellHeight, ((MazeState) path.get(i)).getCurrentPosition().GetColumnIndex() * cellWidth, cellHeight, cellWidth);
            }
        }
        else
        {
            throw  new NotImplementedException();
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

    public String getImageFilePath() {
        return ImageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.ImageFilePath.set(imageFilePath);
        if(this.ImageFilePath.getValue().toString().equals("EMPTY"))
        {
            System.out.println("EMPTY");
            this.isPathImageExist=false;
        }
        else
        {
            this.isPathImageExist=true;
        }
    }

    public void setImageFileNameStart(String imageFileNameStart) {
        this.ImageFileNameStart.set(imageFileNameStart);
    }
    public String getImageFileNameStart() {
        return ImageFileNameStart.get();
    }

    public void setImageFileNameEnd(String imageFileNameEnd) {
        this.ImageFileNameEnd.set(imageFileNameEnd);
    }
    public String getImageFileNameEnd() {
        return ImageFileNameEnd.get();
    }

    public void setImageFileNameSol(String imageFileNameEnd) {
        this.ImageFileNameSol.set(imageFileNameEnd);
    }
    public String getImageFileNameSol() {
        return ImageFileNameSol.get();
    }

    //endregion

}
