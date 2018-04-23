import processing.core.*;

public class BombCell extends Cell {

    /**
     * a variable used to modify the alpha of a flag on a bomb cell
     * this is made lesser when a flagged bomb cell is opened
     * to keep with the overall darkening of the board upon the game ending
     */
    private int flagA;

    public BombCell(PApplet pApplet) {
        super(pApplet, 0, 255, 0);
        this.flagA = 255;
    }

    /**
     * based on state variables of the cell, displays the cell appropriately
     *
     * @param x    the x location to be displayed at
     * @param y    the y location to be displayed at
     * @param size the size to be displayed at
     */
    public void display(float x, float y, float size) {

        p.fill(r, g, b);
        p.rect(x, y, size, size);

        if (flagged) {
            p.fill(flagA);
            p.ellipseMode(PApplet.CENTER);
            p.ellipse(x + size / 2, y + size / 2, size / 2, size / 2);
        }
        if (opened) {
            r = 150;
            g = 0;
            flagA = 150;
        }

    }
}