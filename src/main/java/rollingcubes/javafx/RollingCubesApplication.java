package rollingcubes.javafx;

import java.util.List;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;

import javax.inject.Inject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rollingcubes.results.GameResultDao;
import util.guice.PersistenceModule;

/**
 * Game Javafx Application.
 */
public class RollingCubesApplication extends Application {
    private final Logger logger = LoggerFactory.getLogger(RollingCubesApplication.class);

    private GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                     install(new PersistenceModule("rolling-cubes"));
                     bind(GameResultDao.class);
                 }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting application");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/fxml/opening.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Rolling Cubes");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

}
