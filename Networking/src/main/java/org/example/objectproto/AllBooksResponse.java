package org.example.objectproto;

import org.example.Book;

public class AllBooksResponse implements Response{
    private final Iterable<Book> books;

    public AllBooksResponse(Iterable<Book> books) {
        this.books = books;
    }

    public Iterable<Book> getBooks() {
        return books;
    }
}
