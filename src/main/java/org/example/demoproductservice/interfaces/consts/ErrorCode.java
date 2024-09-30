package org.example.demoproductservice.interfaces.consts;

public enum ErrorCode {
    ;

    public final String message;
    public final Long code;

    ErrorCode(final String message, final Long code) {
        this.message = message;
        this.code = code;
    }

}
