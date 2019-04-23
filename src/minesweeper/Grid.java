package minesweeper;

import java.util.Arrays;
import java.util.Random;

public class Grid {
    enum GameStatus {
        ONGOING, LOSE, WIN;
    }

    private final int MAP_MINE = -1;
    private final int MAP_FRAME = -2;

    enum Tag {
        UNREVEALED, REVEALED, MINE, QUESTION;
    }

    private int height, width, squares, mines, currentMines;
    private GameStatus gameStatus;
    private int[][] mineMap;
    private Tag[][] tagMap;

    public Grid(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
        squares = height * width;
        currentMines = mines;
        gameStatus = GameStatus.ONGOING;
        mineMap = generateMineMap(height, width, squares, mines);
        tagMap = generateTagMap(height, width);
    }

    private int[][] generateMineMap(int height, int width, int squares, int mines) {
        mineMap = new int[height + 2][width + 2];
        // frame mineMap
        for (int[] row : mineMap)
            Arrays.fill(row, MAP_FRAME);
        // generate mines
        Random gen = new Random();
        int rand;
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                rand = gen.nextInt(squares);
                if (rand < mines) {
                    mineMap[i][j] = MAP_MINE;
                    mines--;
                } else {
                    mineMap[i][j] = 0;
                }
                squares--;
            }
        }
        // generate adjacent mine count
        int s;
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (mineMap[i][j] == MAP_MINE)
                    continue;
                s = 0;
                for (int di = -1; di <= 1; di++)
                    for (int dj = -1; dj <= 1; dj++)
                        if (mineMap[i + di][j + dj] == MAP_MINE)
                            s++;
                mineMap[i][j] = s;
            }
        }
        // print mineMap
        for (int[] row : mineMap) {
            for (int x : row) {
                System.out.printf("%2d ", x);
            }
            System.out.printf("%n");
        }
        return mineMap;
    }

    private Tag[][] generateTagMap(int height, int width) {
        tagMap = new Tag[height + 2][width + 2];
        for (Tag[] row : tagMap)
            Arrays.fill(row, Tag.UNREVEALED);
        return tagMap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSquares() {
        return squares;
    }

    public int getMines() {
        return mines;
    }

    public int getCurrentMines() {
        return currentMines;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int[][] getMineMap() {
        return mineMap;
    }

    public Tag[][] getTagMap() {
        return tagMap;
    }

}
