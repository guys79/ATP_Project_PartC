package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This class will represent our view.
 * Meaning that it will be responsible on the way thing appear on the screen.
 */
public class MyViewController  implements Observer, IView {

    @FXML
    private MyViewModel viewModel; // The view model
    private Stage primaryStage; //The primary stage
    public MazeDisplayer mazeDisplayer;// The maze displayer ("our widget")
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    private JTextField filename = new JTextField();
    private JTextField dir = new JTextField();
    public BorderPane BorderPaneId;
    //The string the we bind
    private StringProperty characterPositionRow = new SimpleStringProperty();
    private StringProperty characterPositionColumn = new SimpleStringProperty();

    /**
     * This function will set the viewModel of this view
     * @param viewModel - The viewModel
     */
    public void setViewModel(MyViewModel viewModel) {

        this.viewModel = viewModel;//Set the view model to the given model

        //Binding the labels to the string property in the viewModel
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRowProperty());
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumnProperty());
    }
    /**
     * This function will display a given maze
     * @param maze - The maze is a two dimensional array that contains 0 and 1
     */
    public void displayMaze(int[][] maze)
    {
        mazeDisplayer.setMaze(maze);//Display the maze using the maze displayer (our widget)

        //Get the position of the character
        int characterPositionRow = viewModel.getIntCharacterPositionRow();
        int characterPositionColumn =viewModel.getIntCharacterPositionCol();

        //Display the character
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);

        //Display the character's location
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");

        if(this.viewModel.lastChangeBecauseOfSolve())
        {
            this.mazeDisplayer.drawSolution(this.viewModel.getSol());
        }
    }

    public void solveMaze(){
        this.viewModel.solveMaze();
     /*   ArrayList<AState> path=new ArrayList<>();
        path.add(new MazeState(new Position(1,1)));
        path.add(new MazeState(new Position(2,1)));
        path.add(new MazeState(new Position(3,1)));
        path.add(new MazeState(new Position(4,1)));
        path.add(new MazeState(new Position(5,1)));
        path.add(new MazeState(new Position(6,1)));
        path.add(new MazeState(new Position(7,2)));
        path.add(new MazeState(new Position(7,3)));
        path.add(new MazeState(new Position(7,4)));
        path.add(new MazeState(new Position(7,5)));

        Solution sol=new Solution(path);
        mazeDisplayer.drawSolution(sol);
        */
    }

    /**
     * This function will handle the call of the observerable
     * @param o - The observable
     * @param arg - The runtime argument
     */
    @Override
    public void update(Observable o, Object arg) {
        //If the observable is our model (right now we don't care about other observables
        if (o == viewModel) {

            //Display the maze
            displayMaze(viewModel.getMaze());
            //Allow to the user to keep generating maze

            btn_generateMaze.setDisable(false);
        }
    }



    /**
     * This function is responsible for the management of the key event
     * @param keyEvent - The key event
     */
    public void KeyPressed(KeyEvent keyEvent) {
        //Tell the modelView that a character was moved
        this.viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }



    /**
     * This function is responsible to generate the maze (or rather, tell the view model to generate the maze)
     */
    public void generateMaze() {
        //Receive the size of the maze
        int height = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        //Until we generate the maze we are not allowing the user to generate another maze
        btn_generateMaze.setDisable(true);
        //Generate the maze
        viewModel.generateMaze(width, height);
    }



    /**
     * This function will return the value of the string property
     * @return - The row position
     */
    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }



    /**
     * This function will return the string property
     * @return - The row position
     */
    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }



    /**
     * This function will return the value of the string property
     * @return - The column position
     */
    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }



    /**
     * This function will return the string property
     * @return - The column position
     */
    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    /**
     * This function will be responsible for the "About" action
     * @param actionEvent
     */
    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("AboutController");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    /**
     * This function is responsible to load a maze from the computer
     */
    public void loadMaze()
    {

        //Receiving the path and than creating actual file there
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showOpenDialog(primaryStage);
        System.out.println(file.getAbsolutePath());
        this.viewModel.loadMaze(file.getAbsolutePath());
    }

    /**
     * This function is responsible to save a maze on the computer
     */
    public void saveMaze()
    {


        //Receiving the path and than creating actual file there
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showSaveDialog(primaryStage);
        System.out.println(file.getAbsolutePath());
        this.viewModel.saveMaze(file.getAbsolutePath());


    }


    /**
     * This function is responsible to exit the app
     */
    public void exit()
    {
        primaryStage.close();


    }

    @FXML
    /**
     * This function will change the style of the maze
     */
    public void changeStyle(ActionEvent event)
    {
        //Receiving the kind of style that the user wants
        javafx.scene.control.MenuItem node = (javafx.scene.control.MenuItem) event.getSource() ;
        String data =  (String)node.getUserData();

        //In each case we will select differently
        switch (data) {
            case "classic":
            {

            }
            case "Irish":
            {

                BorderPaneId.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("resources/Images/leprekon/LeprekonTheme.jpg").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                mazeDisplayer.setImageFileNameCharacter("src\\resources\\Images\\leprekon\\character.jpg");
                mazeDisplayer.setImageFileNameEnd("src\\resources\\Images\\leprekon\\end.jpg");
                mazeDisplayer.setImageFileNameWall("src\\resources\\Images\\leprekon\\wall.jpg");
               mazeDisplayer.setImageFilePath("EMPTY");

                mazeDisplayer.redraw();
            }
            case "style2":
            {

            }

        }

    }

//Has a potential to solve the size problem
public void setResizeEvent(Scene scene) {
    throw new NotImplementedException();
}

public void setPrimaryStage(Stage primaryStage)
{
    this.primaryStage=primaryStage;
}

}
