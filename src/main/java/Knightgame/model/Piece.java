package Knightgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Piece.
 */
public class Piece {
    private final PieceType type;
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>();

    /**
     * constructor.
     * @param type The type of the knight.
     * @param position The position of the knight.
     */
    public Piece(PieceType type, Position position) {
        this.type = type;
        this.position.set(position);
    }

    /**
     * getter.
     * @return tpye
     */
    public PieceType getType() {
        return type;
    }

    /**
     * getter.
     * @return position
     */
    public Position getPosition() {
        return position.get();
    }

    /**
     * Set the old position to the new position.
     * @param direction The row and col changes.
     */
    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    /**
     * We can track the position.
     * @return the position
     */
    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    /**
     * Formating.
     * @return the format of the piece.
     */
    public String toString() {
        return type.toString() + position.get().toString();
    }

    /**
     * Main.
     * @param args arguments.
     */
    public static void main(String[] args) { }
}


