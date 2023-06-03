package org.example.objectproto.requestsResponses;

import org.example.Book;

public class SearchedBooksResponse implements Response{
    private final Iterable<Book> books;

    public SearchedBooksResponse(Iterable<Book> books) {
        this.books = books;
    }

    public Iterable<Book> getBooks() {
        return books;
    }
}
