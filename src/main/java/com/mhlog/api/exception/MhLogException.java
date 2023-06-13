package com.mhlog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MhLogException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public MhLogException(String message) {
        super(message);
    }

    public MhLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
