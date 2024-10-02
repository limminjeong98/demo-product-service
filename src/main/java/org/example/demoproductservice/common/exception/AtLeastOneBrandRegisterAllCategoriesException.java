package org.example.demoproductservice.common.exception;

public class AtLeastOneBrandRegisterAllCategoriesException extends RuntimeException {
    public AtLeastOneBrandRegisterAllCategoriesException() {
    }

    public AtLeastOneBrandRegisterAllCategoriesException(String message) {
        super(message);
    }
}
