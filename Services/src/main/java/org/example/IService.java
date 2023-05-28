package org.example;

public interface IService {

    Iterable<Book> findAllBooks();

    Iterable<Book> findAllBooksBySearch(String searched);

    Iterable<Borrowing> findAllForReader(Integer id);

    Reader readerLogIn(String email, String password);

    void addBorrowing(Book book, Reader reader);

    void setClient(IObserver client);

    void notifyClients();
}
