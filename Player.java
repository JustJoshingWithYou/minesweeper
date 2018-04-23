/**
 * an interface which defines a viable player as something which can decide on a Turn given a board
 */
interface Player {

    Turn getTurn(Board board);

}
