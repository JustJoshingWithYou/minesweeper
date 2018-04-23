/**
 * a player which uses artificial intelligence to formulate a Turn
 */
public class AIPlayer implements Player {

    public AIPlayer() {

    }

    /**
     * the AI's method of deciding on a Turn (currently, this method will only open random cells - not very effective)
     *
     * @param board the board to be used to make decisions
     * @return the turn decided on by the AI
     */
    public Turn getTurn(Board board) {

        Cell chosenCell = null;
        int move = Turn.OPENING;

        int randomR = (int) (Math.random() * board.getCells().length);
        int randomC = (int) (Math.random() * board.getCells()[0].length);

        chosenCell = board.getCells()[randomR][randomC];

        return new Turn(chosenCell, move);
    }


}
