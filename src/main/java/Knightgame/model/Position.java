package Knightgame.model;


public record Position(int row, int col) {
    /**
     * Gives us the destination of the knight.
     * @param direction Rowchange, and colchange.
     * @return the position after the move
     */
    public Position moveTo(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Formating.
     * @return the format of the position record.
     */
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}
