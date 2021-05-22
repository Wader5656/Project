package Knightgame.javafx;

import java.io.IOException;
import java.util.List;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;
import util.guice.PersistenceModule;
import Knightgame.results.GameResultDao;


/**
 * KnightApplication.
 */
public class KnightApplication extends Application{

    private GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                    install(new PersistenceModule("Knight-game"));
                    bind(GameResultDao.class);
                }
            }
    ));


    /**
     * Opens the opening scene.
     * @param stage Javafx container
     * @throws IOException Throws an IO exception, when there is a problem, with the loading.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Logger.debug("Started opening.fxml");
        context.init();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/opening.fxml"));
        stage.setTitle("Knight-Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
