package Knightgame.javafx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import org.tinylog.Logger;


public class OpeningController {




    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private TextField playerTwoNameTextField;

    @FXML
    private Label errorLabel;

    /**
     * This is the key in the opening scene. It requires 2 names, and if you don't fill the 2 slots,
     * then an error label appears, to show you, that you need to fill it.
     * This is the method, that sets the players name, and starts the game scene too.
     * @param actionEvent The clicking.
     * @throws IOException Throws an IO exception, when there is a problem, with the loading.
     */
    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty()) {
            errorLabel.setText("Please enter two names!");
            Logger.info("The users entered one name, not two!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));

            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerOneName(playerOneNameTextField.getText());
            fxmlLoader.<GameController>getController().setPlayerTwoName(playerTwoNameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("Player1: {}", playerOneNameTextField.getText());
            Logger.info("Player2: {}", playerTwoNameTextField.getText());
            Logger.info("The game has started!! Good luck!");
        }
    }

    /**
     * Exiting the highscores, and the game.
     * @param actionEvent The clicking.
     */
    public void handleExitButton(ActionEvent actionEvent) {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.debug("The program is exiting!");
        Platform.exit();
    }
}
