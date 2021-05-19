package Knightgame.javafx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

/**
 * KnightApplication.
 */
public class KnightApplication extends Application{
    /**
     * Opens the opening scene.
     * @param stage Javafx container
     * @throws IOException Throws an IO exception, when there is a problem, with the loading.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/opening.fxml"));
        Logger.debug("Started opening.fxml");
        stage.setTitle("Knight-Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
