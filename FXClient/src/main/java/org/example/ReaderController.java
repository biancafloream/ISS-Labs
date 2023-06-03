package org.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReaderController implements IObserver {

    private IService server;

    private Reader reader;
    @FXML
    public TableView<Borrowing> tableBooksBorrowings;
    @FXML
    public TableColumn<Borrowing, String> nameColumnBorrowings;
    @FXML
    public TableColumn<Borrowing, String> statusColumnBorrowings;
    @FXML
    public TableColumn<Borrowing, String> authorColumnBorrowings;
    @FXML
    public TableColumn<Borrowing, String> terminalColumnBorrowings;
    @FXML
    public TableColumn<Borrowing, Timestamp> startDateColumnBorrowings;
    @FXML
    public TableColumn<Borrowing, Timestamp> returnDateColumnBorrowings;
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

    public ReaderController() {
    }

    public void setServer(IService server) {
        this.server = server;
        setColumns();
        setBooks();
        setBorrowings();
        this.server.setClient(this);
    }

    private void setColumns() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        terminalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminal().getName()));
        nameColumnBorrowings.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getName()));
        statusColumnBorrowings.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        authorColumnBorrowings.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));
        returnDateColumnBorrowings.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReturnDate()));
        startDateColumnBorrowings.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBorrowingDate()));
        terminalColumnBorrowings.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTerminal().getName()));
    }

    public void setBorrowings() {
        tableBooksBorrowings.getItems().clear();
        ObservableList<Borrowing> borrowings = FXCollections.observableArrayList();
        server.findAllForReader(reader.getId()).forEach(borrowings::add);
        tableBooksBorrowings.getItems().addAll(borrowings);
    }

    public void setBooks() {
        tableBooks.getItems().clear();
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
        Platform.runLater(this::setBooks);
        Platform.runLater(this::setBorrowings);
    }

    public void addToOrder() {
        int index = tableBooks.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Book book = tableBooks.getSelectionModel().getSelectedItem();
            book.setNoOfCopies(book.getNoOfCopies()-1);
            server.addBorrowing(book, reader);
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setContentText("book borrowed; to be returned: " + Timestamp.valueOf(LocalDateTime.now().plusDays(14)));
            confirmation.show();
        }
    }

    public void setReader(Reader reader) {
        this.reader = reader;
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

    public void returnBook(ActionEvent actionEvent) {
        int index = tableBooksBorrowings.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Borrowing borrowing = tableBooksBorrowings.getSelectionModel().getSelectedItem();
            server.returnBook(borrowing);
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setContentText("book to be returned");
            confirmation.show();
        }
    }
}
