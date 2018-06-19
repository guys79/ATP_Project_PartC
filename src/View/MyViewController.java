package View;

import Server.Server;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * This class will represent our view.
 * Meaning that it will be responsible on the way thing appear on the screen.
 */
public class MyViewController  implements Observer, IView {

    @FXML
    public MazeDisplayer mazeDisplayer;// The maze displayer ("our widget")
    public javafx.scene.control.TextField txtfld_rowsNum;//The text field that is responsible for the row
    public javafx.scene.control.TextField txtfld_columnsNum;//The text field that is responsible for the col
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Label lbl_cols;
    public javafx.scene.control.Label lbl_rows;
    public javafx.scene.control.Label presentCol;
    public javafx.scene.control.Label presentRow;
    public javafx.scene.control.Button btn_generateMaze;//Generating the maze
    public javafx.scene.control.Button btn_solveMaze;//Solving the maze
    public MenuItem exit;//The menu item that is responsible for the exit
    public MenuItem menuItemSave;// The menu item that is responsible for saving the maze
    public BorderPane BorderPaneId;//The borderPane
    public VBox vboxLeft;//The vbox on the left
    public VBox vboxUp;//the vbox on the right

    //The string the we bind
    private StringProperty characterPositionRow = new SimpleStringProperty();
    private StringProperty characterPositionColumn = new SimpleStringProperty();
    private String currentStyle = "classic";
    private MediaPlayer mp;//Media player
    private MyViewModel viewModel; // The view model
    private Stage primaryStage; //The primary stage

    /**
     * This function will set the viewModel of this view
     *
     * @param viewModel - The viewModel
     */
    public void setViewModel(MyViewModel viewModel) {
        //Set the background music
        setMusicTheme("/Images/classic/background music.mp3");

        //Setting the action that the menu will take when pressed
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
        btn_solveMaze.setDisable(true);
        this.viewModel.solveMaze();

    }

    /**
     * This function will handle the call of the observerable
     *
     * @param o   - The observable
     * @param arg - The runtime argument
     */
    @Override
    public void update(Observable o, Object arg) {
        //If the observable is our model (right now we don't care about other observables)
        if (o == viewModel) {
            //allowing the user to click on those buttons
            btn_generateMaze.setDisable(false);
            btn_solveMaze.setDisable(false);
            menuItemSave.setDisable(false);


            //Display the maze
            displayMaze(viewModel.getMaze());

            //If the user won
            if (this.viewModel.win()) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("WINNER!!!");
                alert.show();
                setVictoryMusic(currentStyle);
                mazeDisplayer.win();
                btn_solveMaze.setDisable(true);
            }

            //If the player requested to solve the maze
            if (this.viewModel.lastChangeBecauseOfSolve()) {
                this.mazeDisplayer.drawSolution(this.viewModel.getSol());
            }

        }
    }


    /**
     * This function is responsible for the management of the key event
     * @param keyEvent - The key event
     */
    public void KeyPressed(KeyEvent keyEvent) {
        //Tell the modelView that a character was moved
        if (this.viewModel.win())
            return;
        this.viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }


    /**
     * This function is responsible to generate the maze (or rather, tell the view model to generate the maze)
     */
    public void generateMaze() {
        int height=-1;
        int width=-1;
        try {
            //Receive the size of the maze
            height = Integer.valueOf(txtfld_rowsNum.getText());
            width = Integer.valueOf(txtfld_columnsNum.getText());
            if(!(height >= 0 && width >= 0 && height*width>=9 ))
                throw new Exception("The minimal size of the maze is 3X3");
        }
        catch (Exception e)
        {
            Alert error=new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.show();
            return;
        }
        //Until we generate the maze we are not allowing the user to generate another maze
        btn_generateMaze.setDisable(true);
        if (this.viewModel.win())
            setBackgroundMusic(currentStyle);
        //Generate the maze
        viewModel.generateMaze(width, height);
    }

    /**
     * set the background music according to the style
     * @param style - The style
     */
    private void setBackgroundMusic(String style) {
        switch (style) {
            case "classic": {
                setMusicTheme("/Images/classic/background music.mp3");
                break;
            }

            case "Irish": {
                setMusicTheme("/Images/leprekon/background music.mp3");
                break;
            }
            case "harry potter": {
                setMusicTheme("/Images/harry potter/background music.mp3");
                break;
            }
        }
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
     * @param actionEvent - The action event
     */
    public void About(ActionEvent actionEvent) {
        try {

            //The about handle
            Stage stage = new Stage();
            stage.setTitle("About us");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 450 , 120);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();

        } catch (Exception e) {

            Alert error=new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.show();
        }
    }


    /**
     * This function is responsible to load a maze from the computer
     */
    public void loadMaze() {

        //Receiving the path and than creating actual file there
        FileChooser fileChooser = new FileChooser();
        //Defining the .maze to be the end of the file
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file == null)
            return;

        if (file.getAbsolutePath().length() <= 5 || !file.getAbsolutePath().substring(file.getAbsolutePath().length() - 4).equals("maze")) {
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

        //Stop the servers and the media player
        this.viewModel.stopServers();
        mp.stop();

    }

    /**
     * This function is responsible to ask the user if he wants another game
     */
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

    /**
     * This function will change the solving algorithm of the maze
     * @param event
     */
    public void changeAlgorithm(ActionEvent event) {
        javafx.scene.control.MenuItem node = (javafx.scene.control.MenuItem) event.getSource();
        String data = (String) node.getUserData();
        this.setAlgorithm(data);
    }

    /**
     * this function will give the user to choose what algorithm to use
     */
    private void setAlgorithm(String searchingAlgorhithm) {
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
     * This function will ser the victory music according to the style
     * @param style - The style
     */
    private void setVictoryMusic(String style) {

        switch (style) {
            case "classic": {
                setMusicTheme("/Images/classic/win music.mp3");
                break;
            }
            case "Irish": {
                setMusicTheme("/Images/leprekon/win music.mp3");
                break;
            }
            case "harry potter": {

                setMusicTheme("/Images/harry potter/win music.mp3");
                break;
            }
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


                mazeDisplayer.setImageFileNameCharacter("/Images/classic/character1.jpg");
                mazeDisplayer.setImageFileNameEnd("/Images/classic/endClassic.jpg");
                mazeDisplayer.setImageFileNameWall("/Images/classic/wall4.jpg");
                mazeDisplayer.setImageFileNameStart("/Images/classic/startClassic.png");
                mazeDisplayer.setImageFilePath("/Images/classic/path1.jpg");
                mazeDisplayer.setImageFileNameSol("/Images/classic/sol.jpg");
                mazeDisplayer.setImageFileNameWin("/Images/classic/win.jpg");

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

                lbl_rows.setFont(Font.font("Vardana", FontWeight.NORMAL, 12));
                lbl_cols.setFont(Font.font("Vardana", FontWeight.NORMAL, 12));
                lbl_rows.setTextFill(Color.BLACK);
                lbl_cols.setTextFill(Color.BLACK);
                lbl_rows.setStyle("-fx-background-color: bisque");
                lbl_cols.setStyle("-fx-background-color: bisque");
                BorderPaneId.setStyle(null);
                BorderPaneId.setStyle("-fx-background-color: bisque");

                if (this.viewModel.win()) {
                    setMusicTheme("/Images/classic/win music.mp3");
                } else {
                    setMusicTheme("/Images/classic/background music.mp3");
                }
                currentStyle = "classic";
                break;
            }
            case "Irish": {

                BorderPaneId.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getResource("/Images/leprekon/LeprekonTheme.jpg").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                mazeDisplayer.setImageFileNameCharacter("/Images/leprekon/character.jpg");
                mazeDisplayer.setImageFileNameEnd("/Images/leprekon/end.jpg");
                mazeDisplayer.setImageFileNameWall("/Images/leprekon/wall.jpg");
                mazeDisplayer.setImageFileNameStart("/Images/leprekon/leprekonStart.jpg");
                mazeDisplayer.setImageFilePath("/Images/leprekon/path.jpg");
                mazeDisplayer.setImageFileNameSol("/Images/leprekon/sol.jpg");
                mazeDisplayer.setImageFileNameWin("/Images/leprekon/win.jpg");
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

                lbl_rows.setFont(Font.font("Vardana", FontWeight.BOLD, 12));
                lbl_cols.setFont(Font.font("Vardana", FontWeight.BOLD, 12));
                lbl_rows.setTextFill(Color.RED);
                lbl_cols.setTextFill(Color.RED);
                lbl_rows.setStyle("-fx-background-color: black");
                lbl_cols.setStyle("-fx-background-color: black");
                if (this.viewModel.win()) {
                    setMusicTheme("/Images/leprekon/win music.mp3");
                } else {
                    setMusicTheme("/Images/leprekon/background music.mp3");
                }
                currentStyle = "Irish";
                break;

            }
            case "harry potter": {
                BorderPaneId.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getResource("/Images/harry potter/background2.jpg").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                mazeDisplayer.setImageFileNameCharacter("/Images/harry potter/character2.jpg");
                mazeDisplayer.setImageFileNameEnd("/Images/harry potter/end.jpg");
                mazeDisplayer.setImageFilePath("/Images/harry potter/wall.jpg");
                mazeDisplayer.setImageFileNameStart("/Images/harry potter/start.jpg");
                mazeDisplayer.setImageFileNameWall("/Images/harry potter/path.jpg");
                mazeDisplayer.setImageFileNameSol("/Images/harry potter/sol.jpg");
                mazeDisplayer.setImageFileNameWin("/Images/harry potter/win.jpg");

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
                lbl_rows.setFont(Font.font("Vardana", FontWeight.BOLD, 12));
                lbl_cols.setFont(Font.font("Vardana", FontWeight.BOLD, 12));
                lbl_rows.setTextFill(Color.RED);
                lbl_cols.setTextFill(Color.RED);
                lbl_rows.setStyle("-fx-background-color: black");
                lbl_cols.setStyle("-fx-background-color: black");
                if (this.viewModel.win()) {

                    setMusicTheme("/Images/harry potter/win music.mp3");
                } else {
                    setMusicTheme("/Images/harry potter/background music.mp3");
                }
                currentStyle = "harry potter";
            }

        }
        //Draw the changes
        mazeDisplayer.redraw();
    }

    /**
     * This function will set the way this application will handle a resize event
     * @param scene
     */
    public void setResizeEvent(Scene scene) {


        //The width changes
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                mazeDisplayer.setWidth(newSceneWidth.doubleValue() / 8 * 6);

                if (viewModel.win()) {
                    mazeDisplayer.win();
                } else {
                    mazeDisplayer.redraw();
                }


            }
        });
        //The height changes
        scene.heightProperty().addListener(new ChangeListener<Number>() {
                                               @Override
                                               public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                                                   mazeDisplayer.setHeight(newSceneHeight.doubleValue() / 7 * 6);

                                                   if (viewModel.win()) {
                                                       mazeDisplayer.win();
                                                   } else {
                                                       mazeDisplayer.redraw();
                                                   }
                                               }
                                           }
        );
    }

    /**
     * This function will set the primary stage
     * @param primaryStage - The primary stage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * This function will set the music in the background using a given path
     * @param path - The given path
     */
    private void setMusicTheme(String path) {
        //If there is a mediaPlayer, stop it
        if (mp != null) {

            mp.stop();
        }
        //Create a new media player using the given path
        Media m=null;
        try{
            m = new Media(getClass().getResource(path).toURI().toString());
        }
        catch (Exception e){
            System.out.println(path);
        }
        mp = new MediaPlayer(m);
        //Play the music
        mp.play();
    }


}
