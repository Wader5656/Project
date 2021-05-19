package Knightgame.model;

/**
 * Position.
 */
public record Position(int row, int col) {
    /**
     * Gives us the destination of the knight.
     * @param direction a direction that specifies a change in the coordinates
     * @return The new position's row, and col.
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
