import processing.core.*;

/**
 * a player which uses the user's input to formulate a turn
 */
public class HumanPlayer implements Player {

    /**
     * the Processing Applet required to track the user's input
     */
    private PApplet p;

    public HumanPlayer(PApplet p) {

        this.p = p;
    }

    /**
     * the human player's method of deciding on a Turn
     * it decides the chosen cell based on the mouse's location, and the move based on the mouse button or key pressed
     *
     * @param board the board to be used to make decisions
     * @return the Turn decided on by the human
     */
    public Turn getTurn(Board board) {

        Cell chosenCell = null;
        int move = -1;

        Cell[][] cells = board.getCells();
        float x = p.mouseX;
        float y = p.mouseY;
        float cellSize = board.getCellSize();
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {

                if (x > (r * cellSize) && x < ((r * cellSize) + cellSize)) {
                    if (y > (c * cellSize) && y < ((c * cellSize) + cellSize)) {

                        chosenCell = cells[r][c];

                    }
                }
            }
        }

        if (p.mouseButton == PApplet.LEFT && p.keyPressed) {
            move = Turn.SWEEPING;
        } else if (p.mouseButton == PApplet.LEFT) {
            move = Turn.OPENING;
        } else if (p.mouseButton == PApplet.RIGHT) {
            move = Turn.FLAGGING;
        }

        return new Turn(chosenCell, move);

    }


}
