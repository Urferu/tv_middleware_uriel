package com.urferu.tvmiddleware.exception;

public class ShowNotFoundException extends RuntimeException {

    public ShowNotFoundException(Long showId) {
        super("No se encontró el show con id " + showId);
    }
}
