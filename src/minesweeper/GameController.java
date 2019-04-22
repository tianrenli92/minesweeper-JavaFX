package minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameController {
    @FXML
    private Label lblMines;
    private Grid grid;

    public void setConfig(int height, int width, int mines) {
        grid = new Grid(height, width, mines);
        lblMines.setText(String.valueOf(grid.getMines()));
    }


    @FXML
    public void initialize() {

    }
}
