package Model;

import javafx.scene.input.KeyCode;

public interface IModel {

    public void generateMaze(int width, int height);
    public void moveCharacter(KeyCode movement);
    public int[][] getMaze();
    public int getCharacterPositionRow();
    public int getCharacterPositionColumn();
}
