package Model;

import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.util.Observable;
import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import algorithms.search.AState;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class will be our model (The maze).
 */
public class MyModel extends Observable implements IModel {

    private boolean changeCause=false;
    public Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
    public Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    private Maze myMaze;
    private int width;
    private int height;
    private Solution sol=null;
    private int characterPositionRowIndex;//The row index of the character's position
    private int characterPositionColumnIndex;//The column index of the character's position
    private Solution solution;

    /**
     * This function will start the servers
     */
    public void startServer()
    {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }
    /**
     * this function starts the 2 servers
     */
    public void sartServer()
    {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    /**
     * this function should write to a file the current state of the maze
     * @param filePath- where to write the maze
     */
    private void toFile(String filePath){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filePath, "UTF-8");
            //we write the position of the character
            writer.println(characterPositionRowIndex);
            writer.println(characterPositionColumnIndex);
            //we write the position of the start
            writer.println(myMaze.getStartPosition().GetRowIndex());
            writer.println(myMaze.getStartPosition().GetColumnIndex());
            //we write the position of the end
            writer.println(myMaze.getGoalPosition().GetRowIndex());
            writer.println(myMaze.getGoalPosition().GetColumnIndex());
            //we write the size of the maze
            writer.println(height);
            writer.println(width);
            //we write the values of every cell in the maze
            int [][] maze=myMaze.getMaze();
            for (int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    writer.println(maze[i][j]);
                }
                writer.println(",");
            }
            writer.close();
        } catch (Exception var12) {
            var12.printStackTrace();

        }
    }

    /**
     * we extract from the file the current position of the maze
     * @param filePath-from where to extract the maze
     */
    private void fromFile(String filePath){
        try {
            File file = new File(filePath);
            //for(int i=0;i<file.length())
            Scanner sc = new Scanner(file);
            //we extract the position of the character
            characterPositionRowIndex=Integer.parseInt(sc.nextLine());
            characterPositionColumnIndex=Integer.parseInt(sc.nextLine());
            //we extract the start and end positions
            Position start= new Position(Integer.parseInt(sc.nextLine()),Integer.parseInt(sc.nextLine()));
            Position end= new Position(Integer.parseInt(sc.nextLine()),Integer.parseInt(sc.nextLine()));
            //we extract the size of the maze
            int numOfRows=Integer.parseInt(sc.nextLine());
            int numOfColumns=Integer.parseInt(sc.nextLine());
            //we extract the values of the cells in the maze
            int [][] temp= new int[numOfRows][numOfColumns];
            for(int i=0;i<numOfRows;i++) {
                for (int j = 0; j < numOfColumns; j++) {
                    temp[i][j] = Integer.parseInt(sc.nextLine());
                }
                sc.nextLine();
            }
            myMaze= new Maze(temp,start,end);
        }
        catch (FileNotFoundException var12) {
            var12.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    /**
     * This function will generate a new maze
     * @param width - The width of the maze
     * @param height - The height of the maze
     */
    public void generateMaze(int width, int height)
    {
        this.width=width;
        this.height=height;
        changeCause=false;
        try {
            // we make a connection with the server the generates mazes
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        //we create output and input streams that should communicate with the server
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //we create the size of the maze
                        int[] mazeDimensions = new int[]{width, height};
                        //we write to the server the size of the maze
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[height*width+width+height];
                        is.read(decompressedMaze);
                        //we create the maze
                        Maze maze = new Maze(decompressedMaze);
                        myMaze=maze;

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        Position p= myMaze.getStartPosition();
        characterPositionRowIndex=p.GetRowIndex();//The row index of the character's position
        characterPositionColumnIndex=p.GetColumnIndex();//The column index of the character's position
        setChanged();
        notifyObservers();
    }

    /**
     * this function checks if a move is legal move
     * @param row- the row of the position that the character wants to go to
     * @param col- the column of the position that the character wants to go to
     * @return
     */
    public boolean isLegalMoveMazeCheck(int row,int col)
    {
        if(myMaze==null)
            return false;
        int [][] maze=myMaze.getMaze();
        return !(row<0||row==maze.length|| col<0||col==maze[0].length||maze[row][col]==1);
    }

    @Override
    /**
     * this function should move the character
     */
    public void moveCharacter(KeyCode movement) {
        this.changeCause=false;
        int tempRow = characterPositionRowIndex;
        int tempColumn = characterPositionColumnIndex;

        switch (movement) {
            //Up
            case NUMPAD8:
                tempRow--;
                break;
            //Down
            case NUMPAD2:
                tempRow++;
                break;
            //Right
            case NUMPAD6:
                tempColumn++;
                break;
            //Left
            case NUMPAD4:
                tempColumn--;
                break;
            //Right + Down
            case NUMPAD3:
                tempColumn++;
                tempRow++;
                break;
            //Left + Down
            case NUMPAD1:
                tempColumn--;
                tempRow++;
                break;
            //Left + Up
            case NUMPAD7:
                tempColumn--;
                tempRow--;
                break;
            //Right + Up
            case NUMPAD9:
                tempColumn++;
                tempRow--;
                break;
        }
        //we update the position of the character to be the new position
        if (isLegalMoveMazeCheck(tempRow, tempColumn)) {
            characterPositionRowIndex = tempRow;
            characterPositionColumnIndex = tempColumn;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * This function will return the maze
     * @return - The maze
     * The maze is a two dimensional array that contains 0 and 1
     */
    public int[][] getMaze()
    {
        int[][]maze=myMaze.getMaze();

        maze[myMaze.getStartPosition().GetRowIndex()][myMaze.getStartPosition().GetColumnIndex()]=2;
        maze[myMaze.getGoalPosition().GetRowIndex()][myMaze.getGoalPosition().GetColumnIndex()]=3;
        return maze;
    }

    /**
     * This function will return the row position of the character
     * @return - The row position of the character
     */
    public int getCharacterPositionRow()
    {
        return characterPositionRowIndex;
    }

    /**
     * This function will return the column position of the character
     * @return - The column position of the character
     */
    public int getCharacterPositionColumn()
    {
        return characterPositionColumnIndex;
    }

    /**
     * This function will check whether the new move is a legal move or not
     * @param row- the row of the position that the character wants to go to
     * @param col- the column of the position that the character wants to go to
     * @return - Returns weather the move is legal
     */
    public boolean IsLegalMove(int row,int col)
    {

        return isLegalMoveMazeCheck(row,col);
    }

    @Override
    /**
     * this function should solve the maze
     */
    public Solution solveMaze() {
        this.changeCause=true;

        try {
            //we make a connection with the server that solve mazes
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        //we create output and input streams that should communicate with the server
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //we send our maze the server
                        toServer.writeObject(myMaze);
                        toServer.flush();
                        //we get the solution from the server
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        solution=mazeSolution;
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers();
        return solution;
    }

    @Override
    /**
     * checks if a move occurred because of a change of because of other rezone
     */
    public boolean lastChangeBecauseOfSolve() {
        return this.changeCause;
    }

    @Override
    /**
     * we save the maze
     */
    public void saveMaze(String path) {
        this.toFile(path);
    }

    /**
     * we extract the maze from the file
     * @param path-this is the path to the file
     */
    @Override
    public void loadMaze(String path) {
        this.fromFile(path);
    }

    /**
     * checks if the player won
     * @return- true if he won or false if he failed
     */
    @Override
    public boolean win() {
        if(myMaze==null)
            return false;
        return this.myMaze.getGoalPosition().GetRowIndex()==this.characterPositionRowIndex && this.myMaze.getGoalPosition().GetColumnIndex()==this.characterPositionColumnIndex;
    }

    /**
     * this function should give the solution of the maze
     * @return- the solution of the maze
     */
    public Solution getSolution(){
        return solution;
    }

    /**
     * this function should shut down the servers
     */
    public void stopServers()
    {
        if(this.solveSearchProblemServer!=null)
            this.solveSearchProblemServer.stop();
        if(this.mazeGeneratingServer!=null)
            this.mazeGeneratingServer.stop();

    }
}