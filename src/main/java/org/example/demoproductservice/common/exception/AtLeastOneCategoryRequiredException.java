package org.example.demoproductservice.common.exception;

public class AtLeastOneCategoryRequiredException extends RuntimeException {
    public AtLeastOneCategoryRequiredException(String message) {
        super(message);
    }
    public AtLeastOneCategoryRequiredException() {
    }
}
