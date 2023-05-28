package org.example.objectproto;

public class SearchedBooksRequest implements Request{
    private final String searched;

    public String getSearched() {
        return searched;
    }

    public SearchedBooksRequest(String searched) {
        this.searched = searched;
    }
}
