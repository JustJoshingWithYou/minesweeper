import processing.core.*;

public class SafeCell extends Cell {

    /**
     * only safe cells have clues, therefore only they as a subclass contain this variable
     */
    private int clue;

    public SafeCell(PApplet pApplet) {
        super(pApplet, 0, 255, 0);
        this.clue = 0;
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
            p.fill(255);
            p.ellipseMode(PApplet.CENTER);
            p.ellipse(x + size / 2, y + size / 2, size / 2, size / 2);
        } else if (opened) {

            g = 150;

            p.fill(0);
            p.textAlign(PApplet.CENTER);
            p.textSize(15);
            p.text(clue, x + size / 2, y + size / 2);
        }
    }

    public void setClue(int clue) {
        this.clue = clue;
    }

    public int getClue() {
        return clue;
    }


}