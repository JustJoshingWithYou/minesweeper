import processing.core.*;


/**
 * an abstract notion of a cell, with variables and methods which apply to both bomb cells and safe cells
 */
public abstract class Cell {

    protected PApplet p;
    protected boolean opened;
    protected boolean flagged;
    protected int r, g, b;

    public Cell(PApplet p, int r, int g, int b) {
        this.p = p;
        opened = false;
        flagged = false;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    void open() {
        opened = true;
    }

    void flag() {
        flagged = !flagged;
    }

    abstract void display(float x, float y, float size);

    boolean isFlagged() {
        return flagged;
    }

    boolean isOpened() {
        return opened;
    }

}