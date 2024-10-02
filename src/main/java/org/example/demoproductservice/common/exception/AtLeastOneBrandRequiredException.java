package org.example.demoproductservice.common.exception;

public class AtLeastOneBrandRequiredException extends RuntimeException {
    public AtLeastOneBrandRequiredException(String message) {
        super(message);
    }
    public AtLeastOneBrandRequiredException() {
    }


}
