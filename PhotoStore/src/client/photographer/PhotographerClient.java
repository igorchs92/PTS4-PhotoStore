/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.ClientConnector;
import client.IClient;
import client.photographer.ui.PhotographerClientRegisterController;
import client.ui.InterfaceCall;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.ClientType;

/**
 *
 * @author Igor
 */
public class PhotographerClient extends Application implements IClient {

    private static final Logger LOG = Logger.getLogger(PhotographerClient.class.getName());
    private static PhotographerClientRunnable clientRunnable;
    private static final String title = "Photostore Photographer";
    private Stage primaryStage;
    private Scene sceneLogin;
    private Scene sceneRegister;
    private Scene sceneMain;

    @Override
    public void start(Stage stage) throws Exception {
        connectToServer();
        ClientConnector.client = this;
        this.primaryStage = stage;
        sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("../ui/ClientLogin.fxml")));
        setSceneLogin();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public boolean connectToServer() {
        try {
            ClientConnector clientConnector = new ClientConnector();
            if (clientConnector.connectToServer(ClientType.photographer)) {
                clientRunnable = new PhotographerClientRunnable(clientConnector.getSocket());
                ClientConnector.clientRunnable = clientRunnable;
                return true;
            } else {
                InterfaceCall.connectionFailed();
                return false;
            }
        } catch (IOException | ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void setSceneLogin() {
        primaryStage.setScene(sceneLogin);
        primaryStage.setTitle(title + " - Login");
    }

    public void setSceneRegister() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PhotographerClient.class.getResource("ui/PhotographerClientRegister.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle(title + " - Register");
            PhotographerClientRegisterController controller = loader.getController();
            controller.setDialogStage(stage);
            // Show the dialog and wait until the user closes it
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSceneMain() {
        primaryStage.setScene(sceneMain);
        primaryStage.setTitle(title);
    }

    @Override
    public void loggedIn() {
        System.exit(0);
    }

}
