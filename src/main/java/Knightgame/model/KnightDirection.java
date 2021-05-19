package Knightgame.model;


/**
 * Implements the 8 possible move types.
 */
public enum KnightDirection implements Direction {

    LEFT_UP(-1, -2),
    UP_LEFT(-2, -1),
    UP_RIGHT(-2, 1),
    RIGHT_UP(-1, 2),
    RIGHT_DOWN(1, 2),
    DOWN_RIGHT(2, 1),
    DOWN_LEFT(2, -1),
    LEFT_DOWN(1, -2);

    private final int rowChange;
    private final int colChange;

    /**
     * Constructor
     * @param rowChange new row position - old row position
     * @param colChange new col position - old col position
     */
    KnightDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * getter
     * @return the row change.
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * getter
     * @return the col change.
     */
    public int getColChange() {
        return colChange;
    }

    /**
     *  Returns the move's type. It is a good method for testing.
     * @param rowChange new row position - old row position
     * @param colChange new col position - old col position
     * @return the direction
     */
    public static KnightDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

    }

}


