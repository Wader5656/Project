package Knightgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.*;

/**
 * KnightGameModel.
 */
public class KnightGameModel {
    /**
     * Size of the Board.
     */
    public static int BOARD_SIZE = 8;

    /**
     * Switching sides, with enum.
     */
    public enum Player {
        /**
         * 2 player.
         */
        PLAYER1, PLAYER2;

        /**
         * Who comes, after Player1, and 2.
         * @return the next player.
         */
        public Player next(){
            return switch (this){
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }
    }

    private ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<>();

    private final Piece[] pieces;

    /**
     * Define the 2 knights positions, and the starter player.
     */
    public KnightGameModel() {
        this(new Piece(PieceType.RED, new Position(0, 0)),
                new Piece(PieceType.BLUE, new Position(BOARD_SIZE - 1, BOARD_SIZE - 1)));
        nextPlayer.set(Player.PLAYER1);
    }

    /**
     * The constructor.
     * @param pieces the pieces
     */
    public KnightGameModel(Piece... pieces) {
        checkPieces(pieces);
        this.pieces = pieces.clone();
    }

    /**
     * Defines the next player.
     * @return a Player object, with the next player.
     */
    public Player getNextPlayer() {
        return nextPlayer.get();
    }


    /**
     *  Checks, that the piece has already made, or not.
     * @param pieces pieces.
     */
    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (! isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    /**
     * getter.
     * @return pieces.length
     */
    public int getPieceCount() {
        return pieces.length;
    }

    /**
     * getter.
     * @param pieceNumber An index for a number.
     * @return the type of the pieces.
     */
    public PieceType getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    /**
     *  getter.
     * @param pieceNumber An index for a number.
     * @return the position of the pieces.
     */
    public Position getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    /**
     * We can track the positions.
     * @param pieceNumber An index for a number.
     * @return the property of the position.
     */
    public ObjectProperty<Position> positionProperty(int pieceNumber) {
        return pieces[pieceNumber].positionProperty();
    }

    /**
     * Checks that the move is valid, or not.
     * @param pieceNumber An index for a number.
     * @param direction The row and col changes.
     * @return True if the move is valid, and false if not valid.
     */
    public boolean isValidMove(int pieceNumber, KnightDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= pieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = pieces[pieceNumber].getPosition().moveTo(direction);
        if (! isOnBoard(newPosition)) {
            return false;
        }
        for (var piece : pieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the valid moves.
     * @param pieceNumber An index for a number.
     * @return the valid move's list.
     */
    public Set<KnightDirection> getValidMoves(int pieceNumber) {
        EnumSet<KnightDirection> validMoves = EnumSet.noneOf(KnightDirection.class);
        for (var direction : KnightDirection.values()) {
            if (isValidMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Move, and set the next player.
     * @param pieceNumber An index for a number.
     * @param direction row and col changes.
     */
    public void move(int pieceNumber, KnightDirection direction) {
        pieces[pieceNumber].moveTo(direction);
        nextPlayer.set(nextPlayer.get().next()); //Lépés átadás
    }

    /**
     *  Checks, that the position is on the board.
     * @param position position
     * @return true if it is on the board, false if not.
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }


    /**
     * Returns the index of the piece, that is on the position.
     * @param position position
     * @return the index of the piece
     */
    public OptionalInt getPieceNumber(Position position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Formating.
     * @return the format of a piece.
     */
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : pieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }

    /**
     * Main.
     * @param args arguments.
     */
    public static void main(String[] args) {
    }
}
