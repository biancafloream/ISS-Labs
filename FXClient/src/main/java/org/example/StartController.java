package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    
    private IService server;

    public void setServer(IService server) {
        this.server = server;
    }


    public void viewGuest(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guest-view.fxml"));
            Parent root = fxmlLoader.load();
            GuestController controller = fxmlLoader.getController();
            controller.setServer(server);
            Scene scene = new Scene(root, 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("Guest view");
            stage.setScene(scene);
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewLogIn(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
            Parent root = fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            controller.setServer(server);
            Scene scene = new Scene(root, 500, 700);
            Stage stage = new Stage();
            stage.setTitle("Guest view");
            stage.setScene(scene);
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
