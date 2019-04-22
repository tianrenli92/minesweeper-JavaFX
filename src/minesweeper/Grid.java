package minesweeper;

public class Grid
{
    private int height, width, mines;

    public Grid(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMines() {
        return mines;
    }
}
