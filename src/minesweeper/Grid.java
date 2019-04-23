package minesweeper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

public class Grid {
    public enum GameStatus {
        ONGOING, LOSE, WIN;
    }

    public final static int MAP_MINE = -1;
    public final static int MAP_FRAME = -2;

    public enum Tag {
        UNREVEALED, REVEALED, TAGGED, QUESTIONED;
    }

    private int height, width, squares, unrevealedSafeSquares, mines, untaggedMines;
    private GameStatus gameStatus;
    private int[][] mineMap;
    private Tag[][] tagMap;
    private boolean firstReveal;

    public Grid(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
        squares = height * width;
        unrevealedSafeSquares = squares - mines;
        untaggedMines = mines;
        gameStatus = GameStatus.ONGOING;
        tagMap = generateTagMap(height, width);
        firstReveal = false;
    }

    private int[][] generateMineMap(int height, int width, int squares, int mines, int firstX, int firstY) {
        // frame mineMap
        mineMap = new int[height + 2][width + 2];
        for (int[] row : mineMap)
            Arrays.fill(row, MAP_FRAME);
        // generate mines
        int safeSquares = squares - mines;
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
        // promise first reveal is not mine
        if(mineMap[firstX][firstY] == MAP_MINE) {
            swapSafeSquare(mineMap, safeSquares, firstX, firstY);
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

    private void swapSafeSquare(int[][] mineMap, int safeSquares, int firstX, int firstY){
        Random gen = new Random();
        int rand;
        for (int i = 1; i <= height; i++)
            for (int j = 1; j <= width; j++)
                if (mineMap[i][j] == 0) {
                    rand = gen.nextInt(safeSquares);
                    if (rand == 0){
                        // swap mine and safe square
                        mineMap[i][j] = MAP_MINE;
                        mineMap[firstX][firstY] = 0;
                        return;
                    }
                    safeSquares--;
                }
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

    public int getUnrevealedSafeSquares() {
        return unrevealedSafeSquares;
    }

    public int getMines() {
        return mines;
    }

    public int getUntaggedMines() {
        return untaggedMines;
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

    public void reveal(int x, int y){
        // validate
        if(gameStatus != GameStatus.ONGOING)
            return;
        if(tagMap[x][y] != Tag.UNREVEALED)
            return;
        // reveal square
        tagMap[x][y] = Tag.REVEALED;
        // check mine
        if(!firstReveal) {
            mineMap = generateMineMap(height, width, squares, mines, x, y);
            firstReveal = true;
        }
        if(mineMap[x][y] == MAP_MINE){
            gameStatus = GameStatus.LOSE;
            return;
        }
        unrevealedSafeSquares--;
        // if there is no adjacent mine, reveal nearby safe area
        if(mineMap[x][y] == 0){
            Queue<Integer> qx = new LinkedList<>();
            Queue<Integer> qy = new LinkedList<>();
            int xx, xxx, yy, yyy;
            qx.add(x);
            qy.add(y);
            while(!qx.isEmpty()){
                xx = qx.poll();
                yy = qy.poll();
                for (int dx = -1; dx <= 1; dx++)
                    for (int dy = -1; dy <= 1; dy++){
                        xxx = xx + dx;
                        yyy = yy + dy;
                        if(mineMap[xxx][yyy] == MAP_FRAME || tagMap[xxx][yyy] != Tag.UNREVEALED)
                            continue;
                        tagMap[xxx][yyy] = Tag.REVEALED;
                        unrevealedSafeSquares--;
                        if(mineMap[xxx][yyy] == 0){
                            qx.add(xxx);
                            qy.add(yyy);
                        }
                    }
            }
        }
        checkWin();
    }

    private void checkWin(){
        if(unrevealedSafeSquares == 0){
            gameStatus = GameStatus.WIN;
            // tag all mines
            for (int i = 1; i <= height; i++)
                for (int j = 1; j <= width; j++)
                    if (tagMap[i][j] == Tag.UNREVEALED)
                        tagMap[i][j] = Tag.TAGGED;
        }
    }

    public void tagMap(int x, int y){
        // validate
        if(gameStatus != GameStatus.ONGOING)
            return;
        if(tagMap[x][y] == Tag.REVEALED)
            return;
        // rotation of unrevealed, mined, and questioned
        if(tagMap[x][y] == Tag.UNREVEALED) {
            tagMap[x][y] = Tag.TAGGED;
            untaggedMines--;
            checkWin();
        }
        else if(tagMap[x][y] == Tag.TAGGED) {
            tagMap[x][y] = Tag.QUESTIONED;
            untaggedMines++;
        }
        else if(tagMap[x][y] == Tag.QUESTIONED) {
            tagMap[x][y] = Tag.UNREVEALED;
        }
    }

    public void replay(){
        unrevealedSafeSquares = squares - mines;
        untaggedMines = mines;
        gameStatus = GameStatus.ONGOING;
        tagMap = generateTagMap(height, width);
    }
}
