package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.objectproto.ServicesObjectProxy;


public class StartObjectClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        String serverIP = "127.0.0.1";
        int serverPort = 57864;

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IService server = new ServicesObjectProxy(serverIP, serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader(StartObjectClient.class.getResource("/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        primaryStage.setTitle("App");
        primaryStage.setScene(scene);
        StartController controller = fxmlLoader.getController();
        controller.setServer(server);

        primaryStage.show();

    }
}
