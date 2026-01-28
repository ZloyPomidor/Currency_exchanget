package org.example.exceptions;

public class CurrencyIsAlreadyExistsException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Currencies already exists";

    public CurrencyIsAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public CurrencyIsAlreadyExistsException(String message) {
        super(message);
    }
}
