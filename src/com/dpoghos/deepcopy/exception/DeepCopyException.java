package com.dpoghos.deepcopy.exception;

public class DeepCopyException extends RuntimeException {

    public DeepCopyException(String message) {
        super(message);
    }

    public DeepCopyException(String message, Throwable cause) {
        super(message, cause);
    }
}
