package Knightgame.dao;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;


/**
 *The HighScore class creates a list which contains {@link Score}.
 */

@XmlRootElement(name = "highscores")
public class HighScore {
    private ArrayList<Score> highscore;


    /**
     * Creates an empty instance of {@code HighScore}.
     */
    public HighScore() {
        this.highscore = new ArrayList<Score>();
    }

    /**
     * Sorted the scores and get the first five high scores.
     * @return highscore steps.
     */
    public ArrayList<Score> getHighscore() {
        Collections.sort(highscore, (hs1, hs2) -> {
            return Integer.parseInt(hs2.getSteps()) - Integer.parseInt(hs1.getSteps());
        });
        if (highscore.size() > 5) {
            for (int i = 0; i < highscore.size(); i++) {
                highscore.subList(5, highscore.size()).clear();
            }
        }
        return highscore;
    }

    /**
     * Set the element for the list which contains steps.
     * @param steps {@link ArrayList} which has {@link Score} elements.
     */
    @XmlElement
    public void setHighscore(ArrayList<Score> steps){
        this.highscore = steps;
    }

    /**
     * Add a new {@link Score} to the highscore list.
     * @param steps Steps.
     */

    public void addScore(Score steps){
        highscore.add(steps);
    }
}
