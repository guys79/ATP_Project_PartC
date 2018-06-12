package ViewModel;

import Model.MyModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Observable;
import java.util.Observer;

/**
 * This class will be our view model and will communicate with the model
 */
public class MyViewModel extends Observable implements Observer {

    final String DEFAULT_VALUE="1";
    private StringProperty characterPositionRow = new SimpleStringProperty(DEFAULT_VALUE);
    private StringProperty characterPositionColumn = new SimpleStringProperty(DEFAULT_VALUE);

    public MyViewModel(MyModel model)
    {
        throw new NotImplementedException();
    }

    @Override
    public void update(Observable o, Object arg) {
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

}
