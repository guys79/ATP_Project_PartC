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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


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
    private StringProperty ImageFileNameWin = new SimpleStringProperty();
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
                    Image start = new Image(new FileInputStream(ImageFileNameStart.get()));

                    Image pathImage=null;
                    if(this.isPathImageExist)
                    {
                    pathImage = new Image(new FileInputStream(ImageFilePath.get()));}


                    //Create the maze structure
                    GraphicsContext gc = getGraphicsContext2D();
                    gc.clearRect(0, 0, getWidth(), getHeight());


                    //Draw the maze
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[0].length; j++) {

                            if (maze[i][j] == 1) {
                                gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);

                            }
                            else
                            {
                                if(maze[i][j]==3)
                                {

                                    gc.drawImage(end, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                }
                                else {
                                    if (maze[i][j] == 2) {
                                        gc.drawImage(start, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                    } else {
                                        if (this.ImageFilePath.getValue() != null && isPathImageExist) {

                                            gc.drawImage(pathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                        }
                                    }
                                }
                            }


                        }

                    }





                    //Draw the character
                    gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
                } catch (FileNotFoundException e) {

                    e.printStackTrace();

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
                gc.fillRect(((MazeState) path.get(i)).getCurrentPosition().GetColumnIndex() * cellWidth, ((MazeState) path.get(i)).getCurrentPosition().GetRowIndex() * cellHeight, cellWidth, cellHeight);
            }
        }
        else
        {
            Image solImg=null;
            try {
                solImg = new Image(new FileInputStream(this.ImageFileNameSol.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (; i < path.size() - j; i++) {
                gc.drawImage(solImg,((MazeState) path.get(i)).getCurrentPosition().GetColumnIndex() * cellWidth, ((MazeState) path.get(i)).getCurrentPosition().GetRowIndex() * cellHeight, cellWidth, cellHeight);
            }
        }

    }

    public void win()
    {
        GraphicsContext gc = getGraphicsContext2D();

        if(ImageFileNameWin.getValue()==null)
        {
            gc.setFill(Color.BLUE);
            gc.fillRect(0,0,getHeight(),getWidth());
        }
        else
        {
            try {
                Image winImg = new Image(new FileInputStream(this.ImageFileNameWin.get()));

                gc.drawImage(winImg,0,0,getWidth(),getHeight());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public String location(double relLocRow,double relLocCol)
    {
        if(relLocCol<0 || relLocRow<0)
            return "";
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();

        //Calculate the size of each  cell in the maze
        double cellHeight = canvasHeight / maze.length;
        double cellWidth = canvasWidth / maze[0].length;


        int col=(int)(relLocRow/cellHeight);
        int row=(int)(relLocCol/cellWidth);

        return ""+row+","+col;


    }
    private boolean isLegalMoveString(String loc)
    {
        int row=Integer.parseInt(loc.substring(0,loc.indexOf(",")));
        int col=Integer.parseInt(loc.substring(loc.indexOf(",")+1));
        return  this.isLegalMove(row,col);
    }
    private boolean isLegalMove(int row,int col)
    {
        if(this.maze==null)
            return false;
        return !(row<0||row==maze.length|| col<0||col==maze[0].length||maze[row][col]==1);
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
        if(this.ImageFilePath.getValue().equals("EMPTY"))
        {

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

    public void setImageFileNameSol(String imageFileNameSol) {
        this.ImageFileNameSol.set(imageFileNameSol);
    }
    public String getImageFileNameSol() {
        return ImageFileNameSol.get();
    }

    public void setImageFileNameWin(String imageFileNameWin) {
        this.ImageFileNameWin.set(imageFileNameWin);
    }
    public String getImageFileNameWin() {
        return ImageFileNameWin.get();
    }

    //endregion

}
