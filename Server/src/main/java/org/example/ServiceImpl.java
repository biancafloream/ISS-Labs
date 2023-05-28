package org.example;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceImpl implements IService{

    private final IRepoBooks<Integer, Book> repoBooks;
    private final IRepoBorrowing<Integer, Borrowing> repoBorrowings;
    private final IRepoReader<Integer, Reader> repoReaders;
    private final Repo<Integer, Terminal> repoTerminals;
    private String host;
    private Integer port;

    private Deque<IObserver> clients;

    private final int defaultThreadsNo = 5;

    public ServiceImpl(IRepoBooks<Integer, Book> repoBooks, IRepoBorrowing<Integer, Borrowing> repoBorrowings, IRepoReader<Integer, Reader> repoReaders, Repo<Integer, Terminal> repoTerminals, String host, Integer port) {
        this.repoBooks = repoBooks;
        this.repoBorrowings = repoBorrowings;
        this.repoReaders = repoReaders;
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
    public Reader readerLogIn(String email, String password) {
        String patternEmailLib = "[A-Za-z0-9._]+@lib\\.com";
        Pattern compiledPattern = Pattern.compile(patternEmailLib);
        Matcher matcher = compiledPattern.matcher(email);
        if (!matcher.find()) {
            Reader reader = repoReaders.findOneByEmail(email);
            if (reader != null) {
                if (reader.getPassword().equals(password)) {
                    return reader;
                }
            }
        }
        return null;
    }

    @Override
    public void addBorrowing(Book book, Reader reader) {
        repoBorrowings.save(new Borrowing(book, reader, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plusDays(14))));
        repoBooks.update(book, book.getId());
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
}
