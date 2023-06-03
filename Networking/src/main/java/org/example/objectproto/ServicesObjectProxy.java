package org.example.objectproto;

import org.example.*;
import org.example.objectproto.requestsResponses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ServicesObjectProxy implements IService {

    private final String host;
    private final int port;
    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<>();
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }

    }

    private Response readResponse() {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse update) {
        if (update instanceof UpdateBorrowingsBooks) {
            client.update();
        }
    }

    @Override
    public Iterable<Book> findAllBooks() {
        if (output == null) {
            initializeConnection();
        }
        System.out.println("ceva2");
        try {
            sendRequest(new AllBooksRequest());
            Response response = readResponse();
            if (response instanceof AllBooksResponse) {
                System.out.println(((AllBooksResponse) response).getBooks());
                return ((AllBooksResponse) response).getBooks();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Book> findAllBooksBySearch(String searched) {
        try {
            sendRequest(new SearchedBooksRequest(searched));
            Response response = readResponse();
            if (response instanceof SearchedBooksResponse) {
                System.out.println(((SearchedBooksResponse) response).getBooks());
                return ((SearchedBooksResponse) response).getBooks();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Borrowing> findAllForReader(Integer id) {
        try {
            sendRequest(new ReaderBorrowingsRequest(id));
            Response response = readResponse();
            if (response instanceof ReaderBorrowingsResponse) {
                System.out.println(((ReaderBorrowingsResponse) response).getBorrowings());
                return ((ReaderBorrowingsResponse) response).getBorrowings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setClient(IObserver client) {
        this.client = client;
    }

    @Override
    public Reader readerLogIn(String email, String password) {
        initializeConnection();
        try {
            sendRequest(new LogInReaderRequest(email, password));
            Response response = readResponse();
            if (response instanceof LogInReaderResponse) {
                return ((LogInReaderResponse) response).getReader();
            }
            if (response instanceof ErrorResponse) {
                //closeConnection();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addBorrowing(Book book, Reader reader) {
        try {
            sendRequest(new BorrowingRequest(book, reader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyClients() {

    }

    @Override
    public Librarian librarianLogIn(String email, String password) {
        initializeConnection();
        try {
            sendRequest(new LogInLibrarianRequest(email, password));
            Response response = readResponse();
            if (response instanceof LogInLibrarianResponse) {
                return ((LogInLibrarianResponse) response).getLibrarian();
            }
            if (response instanceof ErrorResponse) {
                //closeConnection();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Borrowing> findAllFromTerminal(Integer id) {
        try {
            sendRequest(new BorrowingsTerminalRequest(id));
            Response response = readResponse();
            if (response instanceof BorrowingsTerminalResponse) {
                System.out.println(((BorrowingsTerminalResponse) response).getBorrowings());
                return ((BorrowingsTerminalResponse) response).getBorrowings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Book> findAllBooksFromTerminal(Integer id) {
        try {
            sendRequest(new BooksTerminalRequest(id));
            Response response = readResponse();
            if (response instanceof BooksTerminalResponse) {
                System.out.println(((BooksTerminalResponse) response).getBooks());
                return ((BooksTerminalResponse) response).getBooks();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addBook(Book book) {
        try {
            sendRequest(new AddBookRequest(book));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book deleteBook(Integer id) {
        try {
            sendRequest(new DeleteBookRequest(id));
            Response response = readResponse();
            if (response instanceof DeleteBookResponse) {
                System.out.println(((DeleteBookResponse) response).getBook());
                client.update();
                return ((DeleteBookResponse) response).getBook();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateBook(Book book, Integer id) {
        try {
            sendRequest(new UpdateBookRequest(book, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(Borrowing borrowing) {
        try {
            sendRequest(new ReturnRequest(borrowing));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void acceptReturn(Borrowing borrowing) {
        try {
            sendRequest(new AcceptReturnRequest(borrowing));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

}

