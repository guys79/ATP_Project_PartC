package View;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Observable;
import java.util.Observer;

/**
 * This class will represent our view.
 * Meaning that it will be responsible on the way thing appear on the screen.
 */
public class MyViewController  implements Observer, IView {

    /**
     * This function will display a given maze
     * @param maze - The maze is a two dimensional array that contains 0 and 1
     */
    public void displayMaze(int[][] maze)
    {
        throw new NotImplementedException();
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new NotImplementedException();
    }
}
