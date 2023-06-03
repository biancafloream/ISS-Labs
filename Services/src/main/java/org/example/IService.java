package org.example;

public interface IService {

    Iterable<Book> findAllBooks();

    Iterable<Book> findAllBooksBySearch(String searched);

    Iterable<Borrowing> findAllForReader(Integer id);

    Reader readerLogIn(String email, String password);

    void addBorrowing(Book book, Reader reader);

    void setClient(IObserver client);

    void notifyClients();

    Librarian librarianLogIn(String email, String password);

    Iterable<Borrowing> findAllFromTerminal(Integer id);
    Iterable<Book> findAllBooksFromTerminal(Integer id);

    void addBook(Book book);
    Book deleteBook(Integer id);
    void updateBook(Book book, Integer id);
    void returnBook(Borrowing borrowing);
    void acceptReturn(Borrowing borrowing);
}
