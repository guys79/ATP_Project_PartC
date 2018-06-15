package View;

import Server.Server;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import java.util.Optional;

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
    public javafx.scene.control.Label presentCol;
    public javafx.scene.control.Label presentRow;
    public javafx.scene.control.Button btn_generateMaze;
    public MenuItem exit;

    public BorderPane BorderPaneId;
    //The string the we bind
    private StringProperty characterPositionRow = new SimpleStringProperty();
    private StringProperty characterPositionColumn = new SimpleStringProperty();

    /**
     * This function will set the viewModel of this view
     *
     * @param viewModel - The viewModel
     */
    public void setViewModel(MyViewModel viewModel) {
        exit.setOnAction(event ->
                primaryStage.fireEvent(
                        new WindowEvent(
                                primaryStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                )
        );
        this.viewModel = viewModel;//Set the view model to the given model

        //Binding the labels to the string property in the viewModel
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRowProperty());
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumnProperty());
    }

    /**
     * This function will display a given maze
     *
     * @param maze - The maze is a two dimensional array that contains 0 and 1
     */
    public void displayMaze(int[][] maze) {
        mazeDisplayer.setMaze(maze);//Display the maze using the maze displayer (our widget)

        //Get the position of the character
        int characterPositionRow = viewModel.getIntCharacterPositionRow();
        int characterPositionColumn = viewModel.getIntCharacterPositionCol();

        //Display the character
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);

        //Display the character's location
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");


    }

    public void solveMaze() {
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
     *
     * @param o   - The observable
     * @param arg - The runtime argument
     */
    @Override
    public void update(Observable o, Object arg) {
        //If the observable is our model (right now we don't care about other observables
        if (o == viewModel) {

            //Display the maze
            displayMaze(viewModel.getMaze());
            //Allow to the user to keep generating maze
            if (this.viewModel.win()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("WINER!!!");
                alert.show();
                mazeDisplayer.win();
            }
            if (this.viewModel.lastChangeBecauseOfSolve()) {
                this.mazeDisplayer.drawSolution(this.viewModel.getSol());
            }
            btn_generateMaze.setDisable(false);
        }
    }


    /**
     * This function is responsible for the management of the key event
     *
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
     *
     * @return - The row position
     */
    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }


    /**
     * This function will return the string property
     *
     * @return - The row position
     */
    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }


    /**
     * This function will return the value of the string property
     *
     * @return - The column position
     */
    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }


    /**
     * This function will return the string property
     *
     * @return - The column position
     */
    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    /**
     * This function will be responsible for the "About" action
     *
     * @param actionEvent
     */
    public void About(ActionEvent actionEvent) {
        try {
            System.out.println("1");
            Stage stage = new Stage();
            System.out.println("2");
            stage.setTitle("AboutController");
            System.out.println("3");
            FXMLLoader fxmlLoader = new FXMLLoader();
            System.out.println("4");
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            System.out.println("5");
            Scene scene = new Scene(root, 400, 350);
            System.out.println("6");
            stage.setScene(scene);
            System.out.println("7");
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            System.out.println("8");
            stage.show();
            System.out.println("9");
        } catch (Exception e) {

            System.out.println("sdjdsa");
        }
    }

    /**
     * This function is responsible to load a maze from the computer
     */
    public void loadMaze() {

        //Receiving the path and than creating actual file there
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null)
            return;
        if (file.getAbsolutePath().length() <= 5 || !file.getAbsolutePath().substring(file.getAbsolutePath().length() - 4).equals(".maze")) {
            Alert unvalidFile = new Alert(Alert.AlertType.ERROR);
            unvalidFile.setContentText("Unvalid file");
            unvalidFile.showAndWait();
            return;
        }
        this.viewModel.loadMaze(file.getAbsolutePath());
    }

    /**
     * This function is responsible to save a maze on the computer
     */
    public void saveMaze() {


        //Receiving the path and than creating actual file there
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null)
            return;
        this.viewModel.saveMaze(file.getAbsolutePath());


    }


    /**
     * This function is responsible to exit the app
     */
    public void exit() {

        this.viewModel.stopServers();

    }

    public void anotherGame() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to play another game?");
        ButtonType yes = new ButtonType("Yes!");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == no) {
            Alert funnyAlert = new Alert(Alert.AlertType.INFORMATION);
            funnyAlert.setContentText("I don't really care... I'll generate another one for you XD");
            funnyAlert.showAndWait();
        }
        this.generateMaze();


    }

    @FXML

    public void changeAlgorithm(ActionEvent event) {
        javafx.scene.control.MenuItem node = (javafx.scene.control.MenuItem) event.getSource();
        String data = (String) node.getUserData();
        this.setAlgorithm(data);
    }

    /**
     * this function will give the user to choose what algorithm to use
     */
    public void setAlgorithm(String searchingAlgorhithm) {
        if (searchingAlgorhithm.equals("bfs")) {
            Server.configurations.set("ASearchingAlgorithm", "bfs");
        }

        if (searchingAlgorhithm.equals("dfs")) {
            Server.configurations.set("ASearchingAlgorithm", "dfs");
        }

        if (searchingAlgorhithm.equals("brfs")) {
            Server.configurations.set("ASearchingAlgorithm", "brfs");
        }
    }

    /**
     * This function will change the style of the maze
     */
    public void changeStyle(ActionEvent event) {
        //Receiving the kind of style that the user wants
        javafx.scene.control.MenuItem node = (javafx.scene.control.MenuItem) event.getSource();
        String data = (String) node.getUserData();
        if (this.viewModel.win()) {
            anotherGame();
        }

        //In each case we will select differently
        switch (data) {
            case "classic": {

                BorderPaneId.setStyle("-fx-background-color: bisque");
                mazeDisplayer.setImageFileNameCharacter("src\\resources\\Images\\classic\\character1.jpg");
                mazeDisplayer.setImageFileNameEnd("src\\resources\\Images\\classic\\endClassic.jpg");
                mazeDisplayer.setImageFileNameWall("src\\resources\\Images\\classic\\wall4.jpg");
                mazeDisplayer.setImageFileNameStart("src\\resources\\Images\\classic\\startClassic.png");
                mazeDisplayer.setImageFilePath("src\\resources\\Images\\classic\\path1.jpg");
                mazeDisplayer.setImageFileNameSol("src\\resources\\Images\\classic\\sol.jpg");
                mazeDisplayer.setImageFileNameWin("src\\resources\\Images\\classic\\win.jpg");

                lbl_columnsNum.setStyle("-fx-background-color: bisque");
                presentCol.setStyle("-fx-background-color: bisque");
                presentRow.setStyle("-fx-background-color: bisque");
                lbl_rowsNum.setStyle("-fx-background-color: bisque");


                lbl_columnsNum.setTextFill(Color.BLACK);
                lbl_rowsNum.setTextFill(Color.BLACK);
                lbl_columnsNum.setFont(Font.font("Vardana", FontWeight.NORMAL, 12));
                lbl_rowsNum.setFont(Font.font("Vardana", FontWeight.NORMAL, 12));

                presentRow.setTextFill(Color.BLACK);
                presentCol.setTextFill(Color.BLACK);
                presentRow.setFont(Font.font("Vardana", FontWeight.NORMAL, 12));
                presentCol.setFont(Font.font("Vardana", FontWeight.NORMAL, 12));


                break;
            }
            case "Irish": {

                BorderPaneId.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("resources/Images/leprekon/LeprekonTheme.jpg").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                mazeDisplayer.setImageFileNameCharacter("src\\resources\\Images\\leprekon\\character.jpg");
                mazeDisplayer.setImageFileNameEnd("src\\resources\\Images\\leprekon\\end.jpg");
                mazeDisplayer.setImageFileNameWall("src\\resources\\Images\\leprekon\\wall.jpg");
                mazeDisplayer.setImageFileNameStart("src\\resources\\Images\\leprekon\\leprekonStart.jpg");
                mazeDisplayer.setImageFilePath("src\\resources\\Images\\leprekon\\path.jpg");
                mazeDisplayer.setImageFileNameSol("src\\resources\\Images\\leprekon\\sol.jpg");
                mazeDisplayer.setImageFileNameWin("src\\resources\\Images\\leprekon\\win.jpg");
                lbl_columnsNum.setStyle("-fx-background-color: black");
                lbl_rowsNum.setStyle("-fx-background-color: black");
                presentCol.setStyle("-fx-background-color: black");
                presentRow.setStyle("-fx-background-color: black");

                lbl_columnsNum.setTextFill(Color.RED);
                lbl_rowsNum.setTextFill(Color.RED);
                lbl_columnsNum.setFont(Font.font("Vardana", FontWeight.BOLD, 12));
                lbl_rowsNum.setFont(Font.font("Vardana", FontWeight.BOLD, 12));

                presentRow.setTextFill(Color.RED);
                presentCol.setTextFill(Color.RED);
                presentRow.setFont(Font.font("Vardana", FontWeight.BOLD, 12));
                presentCol.setFont(Font.font("Vardana", FontWeight.BOLD, 12));

                break;

            }
            case "style2": {
                break;
            }

        }
        mazeDisplayer.redraw();
    }

    //Has a potential to solve the size problem
    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                mazeDisplayer.setWidth(newSceneWidth.doubleValue() / 8 * 6);
                System.out.println(newSceneWidth.doubleValue() / 8 * 6);
                if(viewModel.win())
                {
                    mazeDisplayer.win();
                }
                else
                {
                    mazeDisplayer.redraw();
                }


            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
                                               @Override
                                               public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                                                   System.out.println("Height: " + newSceneHeight);
                                                   mazeDisplayer.setHeight(newSceneHeight.doubleValue() / 7 * 6);
                                                   System.out.println(newSceneHeight.doubleValue() / 7 * 6);
                                                   if(viewModel.win())
                                                   {
                                                       mazeDisplayer.win();
                                                   }
                                                   else
                                                   {
                                                       mazeDisplayer.redraw();
                                                   }
                                               }
                                           }
        );
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


}
