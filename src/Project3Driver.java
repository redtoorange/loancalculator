import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            FXMLLoader loader = new FXMLLoader(  );

            System.out.println( loader.getLocation() );

            Parent root = loader.load(getClass().getResource( "/view/MainView.fxml" ));
            Scene mainScene = new Scene( root );

            primaryStage.setTitle( "Finance Informer 5000" );
            primaryStage.setScene( mainScene );

            primaryStage.show();
            //new ApplicationController( primaryStage );
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println( e.getMessage() );
        }
    }
}
