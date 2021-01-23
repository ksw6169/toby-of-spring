package com.corgi.example.exception;

public class QuerySyntaxException extends RuntimeException {

    public QuerySyntaxException() {
    }

    public QuerySyntaxException(Throwable cause) {
        super(cause);
    }
}
