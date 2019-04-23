package minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameController {
    @FXML
    private Label lblMines;
    private Grid grid;


    @FXML
    public void initialize() {

    }


    // initialize grid
    public void setConfig(int height, int width, int mines) {
        grid = new Grid(height, width, mines);
        lblMines.setText(String.valueOf(grid.getUntaggedMines()));
    }

    // click back button
    @FXML
    private void handleBtnBackAction(ActionEvent event) throws Exception {
        // switch scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Configuration.fxml"));
        Parent gameScene = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(gameScene));
    }

}
