import processing.core.*;

public class Minesweeper {

    private Board board;
    private Player player;

    private int secondsPlaying;

    /**
     * a helper variable which aids in calculating the secondsPlaying using the aggregate System time
     */
    private long timeDifference;

    private int gameScreen;
    private final int START = 0, PLAY = 1, WIN = 2, LOSS = 3;

    private PApplet p;

    public Minesweeper(PApplet p) {

        this.board = null;
        this.player = null;

        this.secondsPlaying = 0;
        this.timeDifference = 0;

        this.gameScreen = START;

        this.p = p;

    }

    /**
     * The run method, which displays and manages all of the game content based on the value of gameScreens
     * also, in the PLAY case, it maintains the timer
     */
    public void run() {

        p.background(0);
        p.frame.setTitle("Josh's Minesweeper");

        switch (gameScreen) {

            case START:

                p.fill(255);
                p.textAlign(PApplet.CENTER, PApplet.CENTER);
                p.textSize(20);

                p.text("Welcome to Josh's Minesweeper!\nThe board will default to 20x20 with 80 bombs\n\nINSTRUCTIONS:\n" +
                                "LEFT CLICK to open a cell\nRIGHT CLICK to flag a cell" +
                                "\nKEYPRESS while LEFT CLICKING to attempt a sweep\n\n" +
                                "LEFT CLICK now to make these decisions yourself\nRIGHT CLICK now to have the AI make decisions",
                        p.width / 2, p.height / 2);


                break;

            case PLAY:

                if (board.bombsSet()) {
                    secondsPlaying = (int) ((System.currentTimeMillis() - timeDifference) / 1000);
                }
                board.display();

                p.fill(255);

                p.textSize(20);

                p.textAlign(PApplet.LEFT, PApplet.CENTER);
                p.text("Seconds Elapsed: " + secondsPlaying, 0, 525);

                p.textAlign(PApplet.RIGHT, PApplet.CENTER);
                p.text("Bombs to find: " + (board.getNumBombs() - board.numCellsFlagged()), p.width, 525);

                break;

            case WIN:

                board.display();

                p.fill(255);

                p.textAlign(PApplet.LEFT, PApplet.CENTER);
                p.text("You Played For: " + secondsPlaying + " Seconds", 0, 525);

                p.textAlign(PApplet.RIGHT, PApplet.CENTER);
                p.text("You Found: " + board.getNumBombs() + " Bombs", p.width, 525);

                p.textAlign(PApplet.CENTER, PApplet.CENTER);
                p.textSize(60);
                p.text("YOU WON!", p.width / 2, p.height / 2 - 25);
                p.textSize(40);
                p.text("CLICK TO RESTART", p.width / 2, p.height / 2 + 25);

                break;

            case LOSS:

                board.display();

                p.fill(255);

                p.textAlign(PApplet.LEFT, PApplet.CENTER);
                p.text("You Played For: " + secondsPlaying + " Seconds", 0, 525);

                p.textAlign(PApplet.RIGHT, PApplet.CENTER);
                p.text("Bombs left to find: " + (board.getNumBombs() - board.numCellsFlagged()), p.width, 525);

                p.textAlign(PApplet.CENTER, PApplet.CENTER);
                p.textSize(60);
                p.text("YOU LOST!", p.width / 2, p.height / 2 - 25);
                p.textSize(40);
                p.text("CLICK TO RESTART", p.width / 2, p.height / 2 + 25);

                break;

        }
    }

    /**
     * ran every time the mouse is clicked, with varying operations based on the value of gameScreen
     */
    public void onMouseClick() {

        switch (gameScreen) {

            /**
             * setting up the game with a user-decided player type, and a new board
             */
            case START:

                if (p.mouseButton == PApplet.LEFT) {
                    player = new HumanPlayer(p);
                } else if (p.mouseButton == PApplet.RIGHT) {
                    player = new AIPlayer();
                }

                board = new Board(p, 20, 20, 80);

                gameScreen = PLAY;

                break;

            case PLAY:

                /**
                 * a player turn will occur with every mouse click
                 * (obviously, this turn will be very much based on the location
                 * and buttons of the mouse if the player is a HumanPlayer)
                 */
                handlePlayerTurn();

                break;

            case WIN:

                gameScreen = START;

                break;

            case LOSS:

                gameScreen = START;

                break;

        }

    }

    /**
     * a routine which to get the player's turn, given the current board
     * the method then acts upon the player's chosen cell with their chosen move
     * given the move committed, this method also checks if the game has been won or lost
     */
    private void handlePlayerTurn() {

        Turn playerTurn = player.getTurn(board);

        if (playerTurn != null) {

            Cell chosenCell = playerTurn.getChosenCell();
            int move = playerTurn.getMove();

            if (chosenCell != null && move != -1) {
                if (move == Turn.OPENING) {
                    handleOpen(chosenCell);
                } else if (move == Turn.FLAGGING) {
                    handleFlag(chosenCell);
                } else if (move == Turn.SWEEPING) {
                    handleSweep(chosenCell);
                }
            }

        }

        if (board.anyBombOpened()) {
            gameScreen = LOSS;
            board.revealEverything();
        }
        if (board.allButBombsOpened()) {
            gameScreen = WIN;
            board.revealEverything();
        }
    }

    /**
     * if board's bombs aren't set yet (i.e. if this is the first cell opened), start the game timer
     * if the given cell isn't flagged, open it (different open methods are called depending on the type of cell)
     *
     * @param cell the given cell
     */
    private void handleOpen(Cell cell) {

        int r = board.getRow(cell);
        int c = board.getColumn(cell);

        if (!cell.isFlagged()) {

            if (!board.bombsSet()) {
                board.placeBombs(r, c);
                timeDifference = System.currentTimeMillis();
            }

            if (cell instanceof BombCell) {
                cell.open();
            } else if (cell instanceof SafeCell) {
                board.openSafeCell(r, c);
            }

        }

    }

    /**
     * flag the given cell if the board has been set
     *
     * @param cell the given cell
     */
    private void handleFlag(Cell cell) {
        if (board.bombsSet()) {

            if (!cell.isOpened()) {
                cell.flag();
            }
        }
    }

    /**
     * sweep from the given cell if the board has been set
     * and the number of bombs truly adjacent to a given cell match the number of flags adjacent to it
     *
     * @param cell the given cell
     */
    private void handleSweep(Cell cell) {
        if (board.bombsSet()) {

            int r = board.getRow(cell);
            int c = board.getColumn(cell);

            if (board.getNumBombsAdjacent(r, c) == board.getNumFlagsAdjacent(r, c)) {
                board.sweep(r, c);
            }
        }
    }

}