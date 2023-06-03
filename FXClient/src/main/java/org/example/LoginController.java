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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String patternEmailLib = "[A-Za-z0-9._]+@lib\\.com";
        Pattern compiledPattern = Pattern.compile(patternEmailLib);
        Matcher matcher = compiledPattern.matcher(email);
        Reader reader = null;
        Librarian librarian = null;
        if (!matcher.find()) {
            reader = server.readerLogIn(email, password);
        } else {
            librarian = server.librarianLogIn(email, password);
        }

        if (reader != null || librarian != null) {
            try {
                Stage stage = new Stage();
                if (reader != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reader-view.fxml"));
                    Parent root = fxmlLoader.load();
                    ReaderController controller = fxmlLoader.getController();
                    controller.setReader(reader);
                    controller.setServer(server);
                    Scene scene = new Scene(root, 1000, 600);

                    stage.setTitle("Reader View");
                    stage.setScene(scene);
                    stage.show();
                }
                else {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/librarian-view.fxml"));
                    Parent root = fxmlLoader.load();
                    LibrarianController controller = fxmlLoader.getController();
                    controller.setLibrarian(librarian);
                    controller.setServer(server);
                    Scene scene = new Scene(root, 1000, 600);

                    stage.setTitle("Librarian View");
                    stage.setScene(scene);
                    stage.show();
                }
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
