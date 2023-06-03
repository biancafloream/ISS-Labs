package org.example.objectproto;

import org.example.*;
import org.example.objectproto.requestsResponses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientObjectWorker implements Runnable, IObserver {

    private final IService server;

    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;

        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    @Override
    public void update() {
        try {
            sendResponse(new UpdateBorrowingsBooks());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Response handleRequest(Request request){
        System.out.println("Got request...");

        if (request instanceof AllBooksRequest){
            System.out.println("All books request ...");
            return new AllBooksResponse(server.findAllBooks());
        }

        if (request instanceof SearchedBooksRequest searchedBooksReq){
            System.out.println("Searched books request ...");
            return new SearchedBooksResponse(server.findAllBooksBySearch(searchedBooksReq.getSearched()));
        }

        if (request instanceof ReaderBorrowingsRequest readerReq){
            System.out.println("Reader's borrowings request ...");
            return new ReaderBorrowingsResponse(server.findAllForReader(readerReq.getId()));
        }

        if (request instanceof BorrowingsTerminalRequest borrowingsReq){
            System.out.println("Reader's borrowings request ...");
            return new BorrowingsTerminalResponse(server.findAllFromTerminal(borrowingsReq.getId()));
        }

        if (request instanceof BooksTerminalRequest booksReq){
            System.out.println("Reader's borrowings request ...");
            return new BooksTerminalResponse(server.findAllBooksFromTerminal(booksReq.getId()));
        }

        if (request instanceof BorrowingRequest borrowingReq){
            System.out.println("Add borrowing request ...");
            server.addBorrowing(borrowingReq.getBook(), borrowingReq.getReader());
            return new UpdateBorrowingsBooks();
        }

        if (request instanceof LogInReaderRequest logReq){
            System.out.println("Login request ...");
            String email=logReq.getEmail();
            String password=logReq.getPassword();

            Reader readerFound = server.readerLogIn(email, password);
            if (readerFound == null) {
                return new ErrorResponse("Wrong credentials");
            } else {
                server.setClient(this);
                return new LogInReaderResponse(readerFound);
            }
        }

        if (request instanceof LogInLibrarianRequest logReq){
            System.out.println("Login request ...");
            String email=logReq.getEmail();
            String password=logReq.getPassword();

            Librarian librarianFound = server.librarianLogIn(email, password);
            if (librarianFound == null) {
                return new ErrorResponse("Wrong credentials");
            } else {
                server.setClient(this);
                return new LogInLibrarianResponse(librarianFound);
            }
        }

        if (request instanceof AddBookRequest addReq) {
            System.out.println("Add book request ...");
            server.addBook(addReq.getBook());
            return new UpdateBorrowingsBooks();
        }

        if (request instanceof UpdateBookRequest updateReq) {
            System.out.println("Update book request ...");
            server.updateBook(updateReq.getBook(), updateReq.getId());
            return new UpdateBorrowingsBooks();
        }
        if (request instanceof DeleteBookRequest deleteReq) {
            System.out.println("Delete book request ...");
            Book book = server.deleteBook(deleteReq.getId());
            if (book == null) {
                return new ErrorResponse("Book doesn't exist");
            }
            else {
                return new DeleteBookResponse(book);
            }
        }

        if (request instanceof ReturnRequest returnReq) {
            System.out.println("Return book request ...");
            server.returnBook(returnReq.getBorrowing());
            return new UpdateBorrowingsBooks();
        }

        if (request instanceof AcceptReturnRequest returnReq) {
            System.out.println("Return book request ...");
            server.acceptReturn(returnReq.getBorrowing());
            return new UpdateBorrowingsBooks();
        }
        return null;
    }

}
