package org.example.objectproto.requestsResponses;

public class SearchedBooksRequest implements Request{
    private final String searched;

    public String getSearched() {
        return searched;
    }

    public SearchedBooksRequest(String searched) {
        this.searched = searched;
    }
}
