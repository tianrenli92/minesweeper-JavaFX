package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent configurationScene = FXMLLoader.load(getClass().getResource("Configuration.fxml"));
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(new Scene(configurationScene));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
