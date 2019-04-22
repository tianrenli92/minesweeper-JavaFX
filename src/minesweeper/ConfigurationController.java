package minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;

public class ConfigurationController {

    @FXML
    private ToggleGroup grpDifficulty;

    @FXML
    private RadioButton rbEasy, rbMedium, rbHard, rbCustom;

    @FXML
    private TextField txtHeight, txtWidth, txtMines;
    private int height, width, mines;

    @FXML
    private Label lblMsg;

    @FXML
    private Button btnPlay;

    // change difficulty
    private void handleGrpDifficultyAction() {
        grpDifficulty.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (grpDifficulty.getSelectedToggle() != null) {
                RadioButton chk = (RadioButton) new_toggle; // Cast object to radio button
                if (chk == rbEasy) {
                    setConfigDisable(true);
                    setConfig("9", "9", "10");
                } else if (chk == rbMedium) {
                    setConfigDisable(true);
                    setConfig("16", "16", "40");
                } else if (chk == rbHard) {
                    setConfigDisable(true);
                    setConfig("16", "30", "99");
                } else if (chk == rbCustom) {
                    setConfigDisable(false);
                }

            }
        });
    }

    private void setConfig(String height, String width, String mines) {
        txtHeight.setText(height);
        txtWidth.setText(width);
        txtMines.setText(mines);
    }

    private void setConfigDisable(boolean disable) {
        txtHeight.setDisable(disable);
        txtWidth.setDisable(disable);
        txtMines.setDisable(disable);
    }

    // click play button
    @FXML
    private void handleBtnPlayAction(ActionEvent event) throws Exception {
        // check integer
        if (!convertString(txtHeight.getText(), txtWidth.getText(), txtMines.getText())) {
            lblMsg.setText("Configs should be integers.");
            return;
        }
        // check range
        if (height < 1 || height > 16) {
            lblMsg.setText("Height should be between 1 and 16.");
            return;
        }
        if (width < 1 || width > 30) {
            lblMsg.setText("Width should be between 1 and 30.");
            return;
        }
        if (mines < 1 || mines >= height * width) {
            lblMsg.setText("Mines should be between 1 and squares - 1.");
            return;
        }

        // switch scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
        Parent gameScene = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(gameScene));

        // pass arguments
        GameController controller = fxmlLoader.<GameController>getController();
        controller.setConfig(height, width, mines);
    }

    private boolean convertString(String strHeight, String strWidth, String strMines) {
        try {
            height = Integer.valueOf(strHeight);
            width = Integer.valueOf(strWidth);
            mines = Integer.valueOf(strMines);

        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @FXML
    public void initialize() {
        handleGrpDifficultyAction();
    }
}
