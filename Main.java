/*
EXTRAS:

-Make an actual algorithm for AIPlayer

-Give the user the ability to modify the size of the grid (this one would be very hard. doable, but hard)

-Give the user the ability to modify the amount of bombs placed

-Implement the ability to pause the game:
the screen goes black, user input is turned off (except to un-pause the game), and the timer is stopped
*/

import processing.core.*;

/**
 * the main class, which is a PApplet subclass
 * exists to run itself with the PApplet.main method, provide a reference of itself to the game,
 * and call the MineSweeper run() and onMouseClick() methods
 */
public class Main extends PApplet {

    private Minesweeper game;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(500, 550);
        game = new Minesweeper(this);
    }

    public void draw() {
        game.run();
    }

    public void mouseClicked() {
        game.onMouseClick();
    }
}