package com.urferu.tvmiddleware.exception;

public class TvMazeClientException extends RuntimeException {

    public TvMazeClientException(String message) {
        super(message);
    }

    public TvMazeClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
