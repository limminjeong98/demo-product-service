package org.example.demoproductservice.common.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(String message) {
        super(message);
    }

    public BrandNotFoundException() {
    }
}
