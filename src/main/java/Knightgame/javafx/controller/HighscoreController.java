package Knightgame.javafx.controller;

import java.io.IOException;
import java.util.List;

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

import Knightgame.results.GameResult;
import Knightgame.results.GameResultDao;

import javax.inject.Inject;

/**
 * Highscore controller.
 */
public class HighscoreController {

    @Inject
    private GameResultDao gameResultDao;

    @FXML
    private TableView<GameResult> highScoreTable;

    @FXML
    private TableColumn<GameResult, String> playerone;

    @FXML
    private  TableColumn<GameResult,String> playertwo;

    @FXML
    private  TableColumn<GameResult,String> winner;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    public HighscoreController() {
    }


    @FXML
    private void initialize(){
        Logger.debug("Loading high scores...");
        List<GameResult> highScoreList = gameResultDao.findBest(10);

        playerone.setCellValueFactory(new PropertyValueFactory<>("playerOneName"));
        playertwo.setCellValueFactory(new PropertyValueFactory<>("playerTwoName"));
        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

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




}

