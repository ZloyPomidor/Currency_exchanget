package org.example.exceptions;

public class ConversionHasBeenFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Conversion has been failed";

    public ConversionHasBeenFailedException(){
        super(DEFAULT_MESSAGE);
    }
    public ConversionHasBeenFailedException(String message) {
        super(message);
    }
}
