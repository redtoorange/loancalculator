import controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Project3Driver.java - Project3Driver Driver for the application.
 *
 * @author Andrew McGuiness
 * @version 29/Mar/2017
 */
public class Project3Driver extends Application {
    public static void main( String[] args ) throws Exception {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage ) throws Exception {
        new ApplicationController( primaryStage );
    }
}
