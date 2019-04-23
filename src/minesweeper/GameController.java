package minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class GameController {
    private static final int SQUARE_WIDTH = 20;
    private static final Color BG_COLOR_UNREVEALED = Color.LIGHTBLUE;
    private static final Color BG_COLOR_TAGGED = Color.YELLOW;
    private static final Color BG_COLOR_QUESTIONED = Color.LIGHTGREEN;
    private static final Color BG_COLOR_REVEALED = Color.WHITE;
    private static final Color BG_COLOR_EXPLODED = Color.RED;
    private static final Color TAG_COLOR = Color.RED;
    private static final Color QUESTION_COLOR = Color.GREEN;
    private static final Color MINE_COLOR = Color.BLACK;
    private static final Color[] NUMBER_COLOR = {Color.WHITE, Color.BLUE, Color.GREEN, Color.RED, Color.DARKBLUE, Color.DARKRED, Color.DARKGREEN, Color.PURPLE, Color.ORANGE};

    @FXML
    private Label lblMines, lblSquares, lblStatus, lblLives;
    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private Grid grid;


    @FXML
    public void initialize() {

    }


    // initialize grid
    public void setConfig(int height, int width, int mines, int lives) {
        grid = new Grid(height, width, mines, lives);
        draw();
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

    @FXML
    private void handleReplayAction(ActionEvent event) throws Exception {
        grid.replay();
        draw();
    }

    // click canvas
    @FXML
    private void handleCanvasMouseClicked(MouseEvent event) {
        double xx = event.getX();
        double yy = event.getY();
        int x = (int) (yy / SQUARE_WIDTH) + 1;
        int y = (int) (xx / SQUARE_WIDTH) + 1;
        if (event.getButton() == MouseButton.PRIMARY) {
            grid.reveal(x, y);
            draw();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            grid.tag(x, y);
            draw();
        } else if (event.getButton() == MouseButton.MIDDLE) {
            grid.quickClear(x, y);
            draw();
        }
    }

    private void draw() {
        lblMines.setText(String.valueOf(grid.getUntaggedMines()));
        lblSquares.setText(String.valueOf(grid.getUnrevealedSafeSquares()));
        lblLives.setText(String.valueOf(grid.getLives()));
        lblStatus.setText(String.valueOf(grid.getGameStatus()));
        if (grid.getGameStatus() == Grid.GameStatus.ONGOING)
            lblStatus.setTextFill(Color.BLACK);
        else
            lblStatus.setTextFill(Color.RED);
        int[][] mineMap = grid.getMineMap();
        Grid.Tag[][] tagMap = grid.getTagMap();
        gc = canvas.getGraphicsContext2D();
        for (int i = 1; i <= grid.getHeight(); i++)
            for (int j = 1; j <= grid.getWidth(); j++) {
                if (tagMap[i][j] == Grid.Tag.UNREVEALED) {
                    drawRectangle(i, j, "", BG_COLOR_UNREVEALED, BG_COLOR_UNREVEALED);
                    continue;
                }
                if (tagMap[i][j] == Grid.Tag.TAGGED) {
                    drawRectangle(i, j, "F", BG_COLOR_TAGGED, TAG_COLOR);
                    continue;
                }
                if (tagMap[i][j] == Grid.Tag.QUESTIONED) {
                    drawRectangle(i, j, "?", BG_COLOR_QUESTIONED, QUESTION_COLOR);
                    continue;
                }
                if (tagMap[i][j] == Grid.Tag.REVEALED) {
                    if (mineMap[i][j] == Grid.MAP_MINE) {
                        drawRectangle(i, j, "X", BG_COLOR_EXPLODED, MINE_COLOR);
                        continue;
                    }
                    drawRectangle(i, j, String.valueOf(mineMap[i][j]), BG_COLOR_REVEALED, NUMBER_COLOR[mineMap[i][j]]);
                    continue;
                }
            }
//        // draw test
//        for (int i = 0; i<=8; i++)
//            for (int j = 0; j<=8; j++)
//            drawRectangle(i, j, String.valueOf(j), BG_COLOR_REVEALED, NUMBER_COLOR[j]);


    }

    private void drawRectangle(int x, int y, String text, Color bgColor, Color textColor) {
        int xx = (y - 1) * SQUARE_WIDTH;
        int yy = (x - 1) * SQUARE_WIDTH;
        // draw boundary
        gc.setFill(Color.BLACK);
        gc.fillRect(xx, yy, SQUARE_WIDTH, SQUARE_WIDTH);

        // draw rectangle
        gc.setFill(bgColor);
        gc.fillRect(xx + 1, yy + 1, SQUARE_WIDTH - 2, SQUARE_WIDTH - 2);

        // draw text
        gc.setStroke(textColor);
        gc.strokeText(text, xx + 6, yy + 15);
    }
}
