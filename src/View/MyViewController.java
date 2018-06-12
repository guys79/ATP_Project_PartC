package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Observable;
import java.util.Observer;

/**
 * This class will represent our view.
 * Meaning that it will be responsible on the way thing appear on the screen.
 */
public class MyViewController  implements Observer, IView {

    @FXML
    private MyViewModel viewModel; // The view model
    public MazeDisplayer mazeDisplayer;// The maze displayer ("our widget")
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;


    //The string the we bind
    private StringProperty characterPositionRow = new SimpleStringProperty();
    private StringProperty characterPositionColumn = new SimpleStringProperty();

    /**
     * This function will set the viewModel of this view
     * @param viewModel - The viewModel
     */
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRowProperty());
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumnProperty());
    }
    /**
     * This function will display a given maze
     * @param maze - The maze is a two dimensional array that contains 0 and 1
     */
    public void displayMaze(int[][] maze)
    {
        throw new NotImplementedException();
    }

    /**
     * This function will handle the call of the observerable
     * @param o - The observable
     * @param arg - The runtime argument
     */
    @Override
    public void update(Observable o, Object arg) {
        throw new NotImplementedException();
    }



    /**
     * This function is responsible for the management of the key event
     * @param keyEvent - The key event
     */
    public void KeyPressed(KeyEvent keyEvent) {
        throw new NotImplementedException();
    }



    /**
     * This function is responsible to generate the maze (or rather, tell the view model to generate the maze)
     */
    public void generateMaze() {
        throw new NotImplementedException();
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

/*

    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });
    }
 */

public void setResizeEvent(Scene scene) {
    throw new NotImplementedException();
}

}
