package Knightgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    Position position;
    PieceType RED;


    @BeforeEach
    void init() {
        position = new Position(0, 0);
        RED = PieceType.RED;

    }


    @Test
    void getType(){
        assertEquals(new Piece(PieceType.RED, new Position(0, 0)).getType(),PieceType.RED);
        assertEquals(new Piece(PieceType.RED, new Position(3, 4)).getType(),PieceType.RED);
        assertEquals(new Piece(PieceType.BLUE, new Position(0, 0)).getType(),PieceType.BLUE);
        assertEquals(new Piece(PieceType.BLUE, new Position(3, 4)).getType(),PieceType.BLUE);
    }
    @Test
    void getPosition(){
        assertEquals(new Piece(PieceType.RED,new Position(0,0)).getPosition(),new Position(0,0));
        assertEquals(new Piece(PieceType.RED,new Position(2,3)).getPosition(),new Position(2,3));
    }


    @Test
    void testToString() {
        assertEquals(new Piece(PieceType.RED, new Position(0, 0)).toString(),"RED(0,0)");
    }
}