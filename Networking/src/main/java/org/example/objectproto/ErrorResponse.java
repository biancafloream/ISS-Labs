package org.example.objectproto;

public class ErrorResponse implements Response{
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
