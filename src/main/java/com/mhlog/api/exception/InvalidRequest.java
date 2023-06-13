package com.mhlog.api.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class InvalidRequest extends MhLogException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
