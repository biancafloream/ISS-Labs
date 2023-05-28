package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwdField;
    private IService server;


    public LoginController() {
    }

    public void setServer(IService server) {
        this.server = server;
    }

    public void logIn(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwdField.getText();

        Reader reader = server.readerLogIn(email, password);
        if (reader != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reader-view.fxml"));
                Parent root = fxmlLoader.load();
                ReaderController controller = fxmlLoader.getController();
                controller.setReader(reader);
                controller.setServer(server);
                Scene scene = new Scene(root, 1000, 600);
                Stage stage = new Stage();
                stage.setTitle("Reader View");
                stage.setScene(scene);
                stage.show();
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Wrong credentials");
            error.show();
        }
    }

}
