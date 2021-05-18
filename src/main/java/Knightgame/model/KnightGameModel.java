package Knightgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.*;


public class KnightGameModel {

    public static int BOARD_SIZE = 8;

    public enum Player {
        PLAYER1, PLAYER2;

        public Player next(){
            return switch (this){
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }
    }

    private ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<>();

    private final Piece[] pieces;

    public KnightGameModel() {
        this(new Piece(PieceType.RED, new Position(0, 0)),
                new Piece(PieceType.BLUE, new Position(BOARD_SIZE - 1, BOARD_SIZE - 1)));
        nextPlayer.set(Player.PLAYER1);
    }
    public KnightGameModel(Piece... pieces) {
        checkPieces(pieces);
        this.pieces = pieces.clone();
    }

    public Player getNextPlayer() {
        return nextPlayer.get();
    }

    public ReadOnlyObjectProperty<Player> nextPlayerProperty(){
        return nextPlayer.getReadOnlyProperty();
    }

    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (! isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }
    public int getPieceCount() {
        return pieces.length;
    }

    public PieceType getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    public Position getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    public ObjectProperty<Position> positionProperty(int pieceNumber) {
        return pieces[pieceNumber].positionProperty();
    }

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

    public Set<KnightDirection> getValidMoves(int pieceNumber) {
        EnumSet<KnightDirection> validMoves = EnumSet.noneOf(KnightDirection.class);
        for (var direction : KnightDirection.values()) {
            if (isValidMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    public void move(int pieceNumber, KnightDirection direction) {
        pieces[pieceNumber].moveTo(direction);
        nextPlayer.set(nextPlayer.get().next()); //Lépés átadás
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    public List<Position> getPiecePositions() {
        List<Position> positions = new ArrayList<>(pieces.length);
        for (var piece : pieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    public OptionalInt getPieceNumber(Position position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : pieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        KnightGameModel model = new KnightGameModel();
        System.out.println(model);
    }
}
