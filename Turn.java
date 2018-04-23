/**
 * a class which encapsulates all data required to decide which operations are to be done onto a cell in a board
 */
public class Turn {

    static int OPENING = 0;
    static int FLAGGING = 1;
    static int SWEEPING = 2;

    private Cell chosenCell;
    private int move;

    public Turn(Cell chosenCell, int move) {
        this.chosenCell = chosenCell;
        this.move = move;
    }

    public Cell getChosenCell() {
        return chosenCell;
    }

    public int getMove() {
        return move;
    }

}
