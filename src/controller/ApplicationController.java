package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * ApplicationController.java - The application controller allows for the decoupling of the application from the main
 * method.
 *
 * @author Andrew McGuiness
 * @version 29/Mar/2017
 */
public class ApplicationController {
    /**
     * After the Application is launched from main, this class handles the setup and shut down of the application.
     *
     * @param mainStage The window handle passed in by main.
     * @throws Exception The program might throw a number of exceptions that could result in the application's crashing.
     *                   Non-fatal exceptions are caught and dealt with.
     */
    public ApplicationController( Stage mainStage ) throws Exception {
        //Load the FXML from the resources folder.
        FXMLLoader loader = new FXMLLoader( getClass().getResource( "../view/MainView.fxml" ) );

        System.out.println( loader.getLocation() );

        Parent root = loader.load();
        Scene mainScene = new Scene( root );

        mainStage.setTitle( "Finance Informer 5000" );
        mainStage.setScene( mainScene );

        mainStage.show();
    }
}
