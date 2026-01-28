package org.example.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrencyNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Currency not found";

    public  CurrencyNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public  CurrencyNotFoundException(String message) {
        super(message);
    }
}
