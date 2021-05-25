package Knightgame.dao;

import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The {@code Score} class implements the score.
 */
@XmlRootElement
public class Score {
    private SimpleStringProperty Winner;
    private SimpleStringProperty steps;


    /**
     * Creates an empty instance of Score.
     */
    public Score() {
        this.Winner = new SimpleStringProperty();
        this.steps = new SimpleStringProperty();

    }

    /**
     * Creates a new instance of Score.
     * @param Winner The winner of the game.
     * @param steps the steps.
     */
    public Score(String Winner, String steps) {
        this.Winner = new SimpleStringProperty();
        this.steps = new SimpleStringProperty();
        this.Winner.set(Winner);
        this.steps.set(steps);

    }

    public String getWinner() { return Winner.get(); }

    public void setWinner(String winner) { this.Winner.set(winner); }

    /**
     * Get the steps.
     *
     * @return Steps.
     */
    public String getSteps() {
        return steps.get();
    }

    /**
     * Set the steps.
     *
     * @param steps Steps.
     */
    @XmlElement
    public void setSteps(String steps) {
        this.steps.set(steps);
    }


    @Override
    public String toString() {
        return "Score{" +
                "Winner=" + Winner +
                "Steps=" + steps +
                '}';
    }
}
