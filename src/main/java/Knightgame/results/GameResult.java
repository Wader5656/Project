package Knightgame.results;

import lombok.*;

import javax.persistence.*;


/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameResult {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the playerOne.
     */
    @Column(nullable = false)
    private String playerOneName;

    /**
     * The name of the playerTwo.
     */
    @Column(nullable = false)
    private String playerTwoName;

    /**
     * The name of the winner.
     */
    @Column(nullable = false)
    private String winner;

    /**
     * The number of steps made by the players.
     */
    private int steps;


}

