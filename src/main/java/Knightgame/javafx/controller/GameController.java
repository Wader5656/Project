package Knightgame.javafx.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


public class GameController {

    @FXML
    private Label mainLabel;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Label stepsLabel;

    @FXML
    private Button giveupButton;

    private KnightGameModel gameState;
    private IntegerProperty steps = new SimpleIntegerProperty();
    private String playerOneName;
    private String playerTwoName;
    public void setPlayerOneName(String playerOneName) {this.playerOneName = playerOneName;}
    public void setPlayerTwoName(String playerTwoName) {this.playerTwoName = playerTwoName;}
    private Position selected;

    private Stage stage;

    private KnightGameModel model = new KnightGameModel();

    private List<Position> invalidPositions = new ArrayList<>();

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

    private void fillinvalidpos (){
        invalidPositions.add(Player1pos);
        invalidPositions.add(Player2pos);
    }

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



    private void createBoard() {
        for (int i = 0; i < gameBoard.getRowCount(); i++) {
            for (int j = 0; j < gameBoard.getColumnCount(); j++) {
                var square = createSquare();
                gameBoard.add(square, j, i);
            }
        }




    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;


    }

    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = createPiece(Color.valueOf(model.getPieceType(i).name()));
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
        }
    }

    private Circle createPiece(Color color) {
        var piece = new Circle(25);
        piece.setFill(color);
        return piece;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

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

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }



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

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    private void showinvalidpositions() {
        for (var invalidPositions : invalidPositions){
            var square = getSquare(invalidPositions);
            square.getStyleClass().add("invalid");
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

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

    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }


    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {

            Logger.info("The game has been given up");
        }

        Logger.debug("Saving result");

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/highscore.fxml"));
        Logger.debug("Loading highscore.fxml");
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }



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
