package org.example.objectproto.requestsResponses;

public class LogInReaderRequest implements Request {
    private final String email;
    private final String password;

    public LogInReaderRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
