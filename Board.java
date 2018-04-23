import processing.core.*;

/**
 * The board for the Minesweeper game -
 * contains many methods for mutating and accessing information about the cells which it contains
 */
public class Board {

    /**
     * The two-dimensional array of cells which make up the board
     */
    private Cell[][] cells;
    /**
     * A helper variable to store the size which the cells are drawn at
     */
    private float cellSize;
    /**
     * Whether or not the bombs have been set on the board yet
     */
    private boolean bombsSet;
    /**
     * The amount of bombs the board contains
     */
    private int numBombs;
    /**
     * The Processing Applet, which exists exclusively to be passed to the cells, which use it for drawing
     */
    private PApplet p;

    public Board(PApplet p, int rows, int columns, int numBombs) {

        this.p = p;

        /**
         * Deciding the cell size by getting the total amount of cells
         * and dividing by a factor of 16 to make the number usable for drawing
         */
        this.cellSize = rows * columns / 16;

        cells = new Cell[rows][columns];

        /**
         * Initially all of the cells are safe
         */
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                cells[r][c] = new SafeCell(this.p);
            }
        }

        this.bombsSet = false;
        this.numBombs = numBombs;
    }

    /**
     * Displays all of the cells based on the calculated size
     */
    public void display() {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                cells[r][c].display(r * cellSize, c * cellSize, cellSize);
            }
        }
    }

    /**
     * Used when the game ends
     * Un-flags and opens all safe cells so that their clue can be seen
     * and opens all bomb cells to reveal their locations
     */
    public void revealEverything() {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {

                if (cells[r][c] instanceof SafeCell) {
                    SafeCell temp = (SafeCell) cells[r][c];
                    temp.setClue(getNumBombsAdjacent(r, c));
                    if (temp.isFlagged()) {
                        temp.flag();
                    }
                }
                cells[r][c].open();
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getNumBombs() {
        return numBombs;
    }

    public float getCellSize() {
        return cellSize;
    }

    public int numCellsFlagged() {
        int flagged = 0;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c].isFlagged()) {
                    flagged++;
                }
            }
        }
        return flagged;
    }

    /**
     * A check if any two given row-column pairs are in the same location or adjacent to eachother
     *
     * @param r1 row 1
     * @param r2 row 2
     * @param c1 column 2
     * @param c2 column 2
     * @return if the two coordinates are equal to each other or adjacent
     */
    private boolean equalOrAdjacentTo(int r1, int r2, int c1, int c2) {

        return Math.abs(r1 - r2) <= 1 && Math.abs(c1 - c2) <= 1;
    }

    /**
     * Randomly places bombs, knowing the location of the initially clicked bomb,
     * so the algorithm will not place bombs adjacent to or on top of that cell
     *
     * @param r the selected row
     * @param c the selected column
     */
    public void placeBombs(int r, int c) {

        if (numBombs == 0)
            return;

        int bombsPlaced = 0;

        while (bombsPlaced < numBombs) {

            int randomR = (int) (Math.random() * cells.length);
            int randomC = (int) (Math.random() * cells[0].length);

            if (!equalOrAdjacentTo(r, randomR, c, randomC)) {
                if (!(cells[randomR][randomC] instanceof BombCell)) {

                    cells[randomR][randomC] = new BombCell(p);

                    bombsPlaced++;
                }
            }
        }

        bombsSet = true;
    }

    public boolean bombsSet() {
        return bombsSet;
    }

    /**
     * An algorithm which runs upon the clicking of a safe cell, deals with setting the clue, and opens the cell
     * and in the case of a zero clue cell, will recurse on the rows and columns of all adjacent cells
     *
     * @param r row of the cell
     * @param c column of the cell
     */
    public void openSafeCell(int r, int c) {

        try {

            SafeCell givenCell = (SafeCell) cells[r][c];

            if (!givenCell.isFlagged()) {

                givenCell.setClue(getNumBombsAdjacent(r, c));
                givenCell.open();

                if (givenCell.getClue() != 0) {
                    return;
                } else if (givenCell.getClue() == 0) {

                    for (int newR = r - 1; newR <= r + 1; newR++) {
                        for (int newC = c - 1; newC <= c + 1; newC++) {

                            if (newR >= 0 && newR < cells.length) {
                                if (newC >= 0 && newC < cells[newR].length) {

                                    if (!(newR == r && newC == c)) {

                                        if (!cells[newR][newC].isOpened()) {
                                            openSafeCell(newR, newC);
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public int getRow(Cell cell) {

        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c].equals(cell)) {
                    return r;
                }
            }
        }

        return -1;
    }

    public int getColumn(Cell cell) {

        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c].equals(cell)) {
                    return c;
                }
            }
        }
        return -1;
    }


    /**
     * A fancy boolean which is used to check if the player has successfully opened all cells but bombs
     *
     * @return if all but bombs are opened
     */
    public boolean allButBombsOpened() {

        boolean allSafeOpened = true;
        boolean allBombsClosed = true;

        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {

                if (cells[r][c] instanceof SafeCell) {
                    if (!cells[r][c].isOpened()) {
                        allSafeOpened = false;
                    }
                } else if (cells[r][c] instanceof BombCell) {
                    if (cells[r][c].isOpened()) {
                        allBombsClosed = false;
                    }
                }

            }
        }

        return allSafeOpened && allBombsClosed;

    }

    /**
     * checks for any bomb being opened, which would trigger losing the game
     *
     * @return if any bomb is opened
     */
    public boolean anyBombOpened() {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c] instanceof BombCell) {
                    if (cells[r][c].isOpened()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * calculates the amount of cells which are bombs adjacent to any row and column
     *
     * @param r the row
     * @param c the column
     * @return the number of adjacent bombs
     */
    public int getNumBombsAdjacent(int r, int c) {

        int bombs = 0;

        for (int newR = r - 1; newR <= r + 1; newR++) {
            for (int newC = c - 1; newC <= c + 1; newC++) {

                if (newR >= 0 && newR < cells.length) {
                    if (newC >= 0 && newC < cells[newR].length) {

                        if (!(newR == r && newC == c)) {

                            if (cells[newR][newC] instanceof BombCell) {
                                bombs++;
                            }

                        }
                    }
                }
            }
        }

        return bombs;

    }
    /**
     * calculates the amount of cells which are flagged adjacent to any row and column
     *
     * @param r the row
     * @param c the column
     * @return the number of adjacent flagged cells
     */
    public int getNumFlagsAdjacent(int r, int c) {

        int flags = 0;

        for (int newR = r - 1; newR <= r + 1; newR++) {
            for (int newC = c - 1; newC <= c + 1; newC++) {

                if (newR >= 0 && newR < cells.length) {
                    if (newC >= 0 && newC < cells[newR].length) {

                        if (!(newR == r && newC == c)) {

                            if (cells[newR][newC].isFlagged()) {
                                flags++;
                            }

                        }
                    }
                }
            }
        }

        return flags;
    }

    /**
     * Opens all adjacent safe cells using the safeCellOpened method and opens all adjacent bomb cells
     * @param r the row
     * @param c the column
     */
    public void sweep(int r, int c) {

        for (int newR = r - 1; newR <= r + 1; newR++) {
            for (int newC = c - 1; newC <= c + 1; newC++) {

                if (newR >= 0 && newR < cells.length) {
                    if (newC >= 0 && newC < cells[newR].length) {

                        if (!(newR == r && newC == c)) {

                            if (cells[newR][newC] instanceof SafeCell) {
                                openSafeCell(newR, newC);
                            } else if (cells[newR][newC] instanceof BombCell && !cells[newR][newC].isFlagged()) {
                                cells[newR][newC].open();
                            }

                        }
                    }
                }
            }
        }

    }
}