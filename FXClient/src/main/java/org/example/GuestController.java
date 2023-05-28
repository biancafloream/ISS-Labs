package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GuestController implements IObserver {

    private IService server;

    @FXML
    public TableView<Book> tableBooks;
    @FXML
    public TableColumn<Book, String> nameColumn;
    @FXML
    public TableColumn<Book, String> authorColumn;
    @FXML
    public TableColumn<Book, String> terminalColumn;
    @FXML
    public TextField searchField;

    public GuestController() {
    }

    public void setServer(IService server) {
        this.server = server;
        setBooks();
        this.server.setClient(this);
    }

    public void setBooks() {
        tableBooks.getItems().clear();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        terminalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminal().getName()));
        ObservableList<Book> books = FXCollections.observableArrayList();
        Iterable<Book> allBooks = server.findAllBooks();
        for (Book book : allBooks) {
            if (book.getNoOfCopies() > 0) {
                books.add(book);
            }
        }
        tableBooks.getItems().addAll(books);
    }

    @Override
    public void update() {

    }

    public void logInGuest(ActionEvent actionEvent) {
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

    public void searchBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        Iterable<Book> searchedBooks = server.findAllBooksBySearch(searchField.getText());
        for (Book book : searchedBooks) {
            if (book.getNoOfCopies() > 0) {
                books.add(book);
            }
        }
        tableBooks.getItems().clear();
        tableBooks.getItems().addAll(books);
    }
}
