package org.example.objectproto.requestsResponses;

import org.example.Borrowing;

public class ReaderBorrowingsResponse implements Response{
    private final Iterable<Borrowing> borrowings;

    public Iterable<Borrowing> getBorrowings() {
        return borrowings;
    }

    public ReaderBorrowingsResponse(Iterable<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }
}
