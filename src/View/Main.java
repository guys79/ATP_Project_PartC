package View;

import Model.*;
import Server.Initialize;
import ViewModel.MyViewModel;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    MyViewController view;
    @Override
    public void start(Stage primaryStage) throws Exception {
       // MyModel model = new MyModel();
        Initialize.init();
        MyModel model=new MyModel();
        model.mazeGeneratingServer.start();
        model.solveSearchProblemServer.start();
       // model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //--------------
        primaryStage.setTitle("My Application!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());

        primaryStage.setScene(scene);
        //--------------
        view = fxmlLoader.getController();

        view.setResizeEvent(scene);


        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //--------------
        view.setPrimaryStage(primaryStage);
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure yow want to quit?");
                ButtonType yes=new ButtonType("Yes");
                ButtonType no=new ButtonType("No");
                alert.getButtonTypes().setAll(yes,no);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == no){

                    windowEvent.consume();
                }
                else
                {
                    view.exit();
                }

            }
        });


    }

    public static void main(String[] args) {
        launch(args);
    }
}
