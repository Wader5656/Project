package Knightgame.javafx.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Knightgame.dao.HighScoreDAO;
import Knightgame.dao.Score;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import Knightgame.model.KnightGameModel;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;
import Knightgame.model.KnightDirection;
import Knightgame.model.Position;






/**
 * Gamecontroller.
 */
public class GameController {

    @FXML
    private Label mainLabel;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Label stepsLabel;

    @FXML
    private Button giveupButton;




    private IntegerProperty steps = new SimpleIntegerProperty();
    private String playerOneName;
    private String playerTwoName;
    private String Winner;

    public void setPlayerOneName(String playerOneName) {this.playerOneName = playerOneName;}
    public void setPlayerTwoName(String playerTwoName) {this.playerTwoName = playerTwoName;}
    private Position selected;


    private KnightGameModel model = new KnightGameModel();

    /**
     * A list, that contais the invalid positions.
     */
    private List<Position> invalidPositions = new ArrayList<>();

    /**
     * Gives a selection phase: From -> To.
     */
    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }
    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position Player1pos = new Position(7,7); // blue
    private Position Player2pos = new Position(0,0); // red

    /**
     * Add the 2 starter positions to {@code invalidPositions}.
     */
    private void fillinvalidpos (){
        invalidPositions.add(Player1pos);
        invalidPositions.add(Player2pos);
    }

    /**
     * It is responsible for starting the game.
     */
    @FXML
    private void initialize() {
        createBoard();
        Logger.debug("Created the Board!");
        createPieces();
        fillinvalidpos();
        setSelectablePositions();
        showSelectablePositions();
        Platform.runLater(() -> mainLabel.setText(String.format("Good luck, %s, and %s!", playerOneName,playerTwoName)));
        stepsLabel.textProperty().bind(steps.asString());
    }

    /**
     * It creates the board.
     */
    private void createBoard() {
        for (int i = 0; i < gameBoard.getRowCount(); i++) {
            for (int j = 0; j < gameBoard.getColumnCount(); j++) {
                var square = createSquare();
                gameBoard.add(square, j, i);
            }
        }
    }

    /**
     * Creates squares.
     * @return A square
     */
    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;


    }

    /**
     * This method creates the 2 knights, to the starter position.
     */
    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = createPiece(Color.valueOf(model.getPieceType(i).name()));
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
        }
    }

    /**
     * Creates 2 circle(knights).
     * @param color The color of the circle.
     * @return The circle.
     */
    private Circle createPiece(Color color) {
        var piece = new Circle(25);
        piece.setFill(color);
        return piece;
    }

    /**
     * This method handle the mouse click. So we can see the square's position.
     * @param event The mouse click.
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

    /**
     * This method is the most important method. It handles the moving system.
     * It shows me the figure i can make the move with, and the possible moves.
     * The {@code gameover} check is also in this method, so the game ends only
     * when a player click on the figure, and it has 0 {@code selectablePositions}.
     * @param position The position of the square.
     */
    private void handleClickOnSquare(Position position) {
        if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1) {
            switch (selectionPhase) {
                case SELECT_FROM -> {
                    if (selectablePositions.contains(position)) {
                        selectPosition(position);
                        alterSelectionPhase();
                    }
                    if (selectablePositions.size() == 0){
                        gameover();
                    }
                }

                case SELECT_TO -> {

                    if (selectablePositions.contains(position)) {
                        var pieceNumber = model.getPieceNumber(selected).getAsInt();
                        var direction = KnightDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        Logger.debug("Moving piece {} {}", pieceNumber, direction);
                        model.move(pieceNumber, direction);
                        invalidPositions.add(new Position(position.row(), position.col()));
                        Player1pos = new Position(position.row(),position.col());
                        showinvalidpositions();
                        Logger.debug("{}, has added to invalidpositions", new Position(position.row(), position.col()));
                        steps.set(steps.get() + 1);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                        Logger.debug("{} következik!", model.getNextPlayer());
                        model.getNextPlayer();

                    }
                }
            }
        }
        else {
            switch (selectionPhase) {
                case SELECT_FROM -> {
                    if (selectablePositions.contains(position)) {
                        selectPosition(position);
                        alterSelectionPhase();
                    }
                    if (selectablePositions.size() == 0){
                        gameover();
                    }
                }
                case SELECT_TO -> {
                    if (selectablePositions.contains(position)) {
                            var pieceNumber = model.getPieceNumber(selected).getAsInt();
                            var direction = KnightDirection.of(position.row() - selected.row(), position.col() - selected.col());
                            Logger.debug("Moving piece {} {}", pieceNumber, direction);
                            model.move(pieceNumber, direction);
                            invalidPositions.add(new Position(position.row(), position.col()));
                            Player2pos = new Position(position.row(), position.col());
                            showinvalidpositions();
                            Logger.debug("{}, has added to invalidpositions", new Position(position.row(), position.col()));
                            steps.set(steps.get() + 1);
                            deselectSelectedPosition();
                            alterSelectionPhase();
                            Logger.debug("{} következik!", model.getNextPlayer());
                            model.getNextPlayer();
                    }
                }
            }
        }
    }

    /**
     * Hide, re set, an show the {@code selectablePositions}.
     */
    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    /**
     * It selects a square based to the {@code position}.
     * @param position The position of the square.
     */
    private void selectPosition(Position position) {
        selected = position;
    }

    /**
     * Deselect the {@code position}.
     */
    private void deselectSelectedPosition() {
        selected = null;
    }

    /**
     * Sets the {@code selectablePositions}, according to the player, and the {@code invalidPositions}.
     */
    private void setSelectablePositions() {
        selectablePositions.clear();
            if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1) {
                switch (selectionPhase) {
                    case SELECT_FROM -> selectablePositions.add(Player1pos);

                    case SELECT_TO -> {
                        var pieceNumber = model.getPieceNumber(selected).getAsInt();

                        for (var direction : model.getValidMoves(pieceNumber)) {
                            if (invalidPositions.contains(selected.moveTo(direction))) {
                                continue;
                            }
                            selectablePositions.add(selected.moveTo(direction));
                        }
                    }
                }
            }
            else{
                switch (selectionPhase) {
                    case SELECT_FROM -> selectablePositions.add(Player2pos);

                    case SELECT_TO -> {
                        var pieceNumber = model.getPieceNumber(selected).getAsInt();

                        for (var direction : model.getValidMoves(pieceNumber)) {
                            if (invalidPositions.contains(selected.moveTo(direction))) {
                                continue;
                            }
                            selectablePositions.add(selected.moveTo(direction));

                        }
                    }
                }
            }

    }

    /**
     * Gives the selectable style to the squares.
     */
    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    /**
     * Gives the invalid style to the squares.
     */
    private void showinvalidpositions() {
        for (var invalidPositions : invalidPositions){
            var square = getSquare(invalidPositions);
            square.getStyleClass().add("invalid");
        }
    }

    /**
     * Remove selectable class from squares.
     */
    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    /**
     * The method returns a square, if the gridpane's row and column indexes is the same with the position's row and
     * column indexes.
     * @param position position of the square.
     * @return Squares.
     */
    private StackPane getSquare(Position position) {
        for (var child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == null || GridPane.getColumnIndex(child) == null){
                continue;
            }
            else if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    /**
     * It notes the {@code position} changes.
     * @param observable Observe the changes of the position.
     * @param oldPosition Last position of the Knight
     * @param newPosition New position of the Knight
     */
    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

    /**
     * Handles give up button. If the game didn't end, then the button has the "Give Up" Words.
     * After the game finished, then it changes to Highscores. This method leads me to the highscore scene,
     * when it has the give up, or the highscores button too.
     * @param actionEvent The clicking.
     * @throws IOException Throws an IO exception, when there is a problem, with the loading.
     */
    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1) {
                Logger.info("The game has been given up by {}",playerOneName);

            }
            else{
                Logger.info("The game has been given up by {}",playerTwoName);

            }
        }
        if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1) {
            Winner = playerTwoName;
        }
        else {
            Winner = playerOneName;
        }
        Logger.debug("Saving results to highscore.xml");
        Score newScore = new Score(Winner, stepsLabel.getText());
        HighScoreDAO highScoreDAO = new HighScoreDAO();
        highScoreDAO.addScore(newScore);
        Logger.debug("Saved!");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/highscore.fxml"));
        Logger.debug("Loading highscore.fxml");
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }


    /**
     * Checks, that are there any possible moves to the knight.
     * If no, then the other player wins the game.
     */
    private void gameover(){
        if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1){
        Platform.runLater(() -> mainLabel.setText(String.format("The Winner is %s!", playerTwoName)));
        Logger.info("{} winned the game!",playerTwoName);
        Platform.runLater(() -> giveupButton.setText(String.format("Highscores")));
        }
        else{
            Platform.runLater(() -> mainLabel.setText(String.format("The Winner is %s!", playerOneName)));
            Logger.info("{} winned the game!",playerOneName);
            Platform.runLater(() -> giveupButton.setText(String.format("Highscores")));
        }
    }






}
