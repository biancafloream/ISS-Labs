package org.example.objectproto;

import org.example.Reader;

public class LogInReaderResponse implements Response {
    private final Reader reader;

    public Reader getReader() {
        return reader;
    }

    public LogInReaderResponse(Reader reader) {
        this.reader = reader;
    }
}
