package Knightgame.javafx.controller;

import java.io.IOException;
import java.util.List;

import Knightgame.dao.HighScore;
import Knightgame.dao.HighScoreDAO;
import Knightgame.dao.Score;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.tinylog.Logger;




/**
 * Highscore controller.
 */
public class HighscoreController {



    @FXML
    private TableView<Score> highScoreTable;

    @FXML
    private TableColumn<Score, String> winner;

    @FXML
    private TableColumn<Score, String> steps;


    /**
     *  Restart the game, after a match finished, and the players check the highscores.
     *  It opens the opening scene.
     * @param actionEvent The clicking
     * @throws IOException Throws an IO exception, when there is a problem, with the loading.
     */
    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/opening.fxml"));
        Logger.debug("Loading Opening.fxml");
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Exiting the highscores, and the game.
     * @param actionEvent The clicking.
     */
    public void handleExitButton(ActionEvent actionEvent)  {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.debug("The program is exiting!");
        Platform.exit();

    }

    /**
     * The soul of the controller.
     * This fits the data's to the cells.
     */
    @FXML
    private void initialize(){
        HighScoreDAO highScoreDao = new HighScoreDAO();
        HighScore hs = new HighScore();
        hs = highScoreDao.getHighScores();
        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        List<Score> highScoreList = hs.getHighscore();
        ObservableList<Score> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);
        highScoreTable.setItems(observableResult);
    }






}

