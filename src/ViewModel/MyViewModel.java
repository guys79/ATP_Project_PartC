package ViewModel;

import Model.IModel;
import Model.MyModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Observable;
import java.util.Observer;

/**
 * This class will be our view model and will communicate with the model
 */
public class MyViewModel extends Observable implements Observer {

    private IModel model;//A model

    private int characterPositionRowIndex;//The row index of the character's position
    private int characterPositionColumnIndex;//The column index of the character's position

    //The default value
    final String DEFAULT_VALUE = "-1";
    private StringProperty characterPositionRow = new SimpleStringProperty(DEFAULT_VALUE);
    private StringProperty characterPositionColumn = new SimpleStringProperty(DEFAULT_VALUE);

    /**
     * The constructor
     *
     * @param model - A given model
     */
    public MyViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new NotImplementedException();
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
     * This function will return the row position of the character
     *
     * @return - An integer that represents the row position of the character in the maze
     */
    public int getIntCharacterPositionRow() {
        return this.characterPositionRowIndex;
    }

    /**
     * This function will return the column position of the character
     *
     * @return - An integer that represents the column position of the character in the maze
     */
    public int getIntCharacterPositionCol() {
        return this.characterPositionColumnIndex;
    }

    /**
     * This function will return the maze
     *
     * @return - a two dimensional array of 0 and 1
     */
    public int[][] getMaze() {
        return model.getMaze();
    }

    /**
     * This function will notify the viewModel about the moving character
     *
     * @param movement - The movement of the character.. the way the character moved
     */
    public void moveCharacter(KeyCode movement) {
        throw new NotImplementedException();
    }

    /**
     * This function will notify the model to generate a maze
     *
     * @param width  - The width of the maze
     * @param height - The height of the maze
     */
    public void generateMaze(int width, int height) {
        throw new NotImplementedException();
    }
}
