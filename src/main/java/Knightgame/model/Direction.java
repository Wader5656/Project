package Knightgame.model;

/**
 * Gets the row change, and column change.
 */
public interface Direction {

    /**
     * Row changes.
     * @return row change as an Integer
     */
    int getRowChange();

    /**
     * Column change.
     * @return col change as an Integer.
     */
    int getColChange();

}
