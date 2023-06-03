package org.example.objectproto.requestsResponses;

public class ReaderBorrowingsRequest implements Request{
    private Integer id;

    public Integer getId() {
        return id;
    }

    public ReaderBorrowingsRequest(Integer id) {
        this.id = id;
    }
}
