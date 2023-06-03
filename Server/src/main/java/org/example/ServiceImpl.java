package org.example;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ServiceImpl implements IService{

    private final IRepoBooks<Integer, Book> repoBooks;
    private final IRepoBorrowing<Integer, Borrowing> repoBorrowings;
    private final IRepoReader<Integer, Reader> repoReaders;
    private final IRepoLibrarians<Integer, Librarian> repoLibrarians;
    private final Repo<Integer, Terminal> repoTerminals;
    private String host;
    private Integer port;

    private Deque<IObserver> clients;

    private final int defaultThreadsNo = 5;

    public ServiceImpl(IRepoBooks<Integer, Book> repoBooks, IRepoBorrowing<Integer, Borrowing> repoBorrowings, IRepoReader<Integer, Reader> repoReaders, IRepoLibrarians<Integer, Librarian> repoLibrarians, Repo<Integer, Terminal> repoTerminals, String host, Integer port) {
        this.repoBooks = repoBooks;
        this.repoBorrowings = repoBorrowings;
        this.repoReaders = repoReaders;
        this.repoLibrarians = repoLibrarians;
        this.repoTerminals = repoTerminals;
        this.host = host;
        this.port = port;
        clients = new ConcurrentLinkedDeque<>();
    }


    @Override
    public Iterable<Book> findAllBooks() {
        return repoBooks.findAll();
    }

    @Override
    public Iterable<Book> findAllBooksBySearch(String searched) {
        return repoBooks.findAllBySearch(searched);
    }

    @Override
    public Iterable<Borrowing> findAllForReader(Integer id) {
        return repoBorrowings.findAllForReader(id);
    }

    @Override
    public Iterable<Borrowing> findAllFromTerminal(Integer id) { return repoBorrowings.findAllFromTerminal(id); }

    @Override
    public Iterable<Book> findAllBooksFromTerminal(Integer id) {
        return repoBooks.findAllByTerminal(id);
    }

    @Override
    public void addBook(Book book) {
        repoBooks.save(book);
        this.notifyClients();
    }

    @Override
    public Book deleteBook(Integer id) {
        Book bookDeleted = repoBooks.delete(id);
        this.notifyClients();
        return bookDeleted;
    }

    @Override
    public void updateBook(Book book, Integer id) {
        repoBooks.update(book, id);
        this.notifyClients();
    }

    @Override
    public void returnBook(Borrowing borrowing) {
        borrowing.setStatus("Pending");
        repoBorrowings.update(borrowing, borrowing.getReader().getId(), borrowing.getBook().getId());
        this.notifyClients();
    }

    @Override
    public void acceptReturn(Borrowing borrowing) {
        borrowing.setStatus("Returned");
        repoBorrowings.update(borrowing, borrowing.getReader().getId(), borrowing.getBook().getId());
        this.notifyClients();
    }

    @Override
    public Reader readerLogIn(String email, String password) {
        Reader reader = repoReaders.findOneByEmail(email);
        if (reader != null) {
            if (reader.getPassword().equals(password)) {
                return reader;
            }
        }
        return null;
    }

    @Override
    public void addBorrowing(Book book, Reader reader) {
        repoBorrowings.save(new Borrowing(book, reader, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plusDays(14)), "Not returned"));
        repoBooks.update(book, book.getId());
        this.notifyClients();
    }

    @Override
    public void setClient(IObserver client) {
        clients.add(client);
    }


    @Override
    public void notifyClients() {
        for (IObserver client: clients) {
            System.out.println("1"+client);
            client.update();
        }
    }

    @Override
    public Librarian librarianLogIn(String email, String password) {
        Librarian librarian = repoLibrarians.findOneByEmail(email);
        if (librarian != null) {
            if (librarian.getPassword().equals(password)) {
                return librarian;
            }
        }
        return null;
    }

}
