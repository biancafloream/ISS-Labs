package org.example.objectproto.requestsResponses;

import org.example.Book;
import org.example.Reader;

public class BorrowingRequest implements Request {
    private Book book;
    private Reader reader;

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public BorrowingRequest(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }
}
