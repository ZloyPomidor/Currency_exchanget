package org.example.exceptions;

public class ExchangeRateNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Exchange rate not found";


    public ExchangeRateNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

    public ExchangeRateNotFoundException(String message) {
        super(message);
    }
}
