package com.thangdm.auth_service.exception;

import lombok.Data;

@Data
public class UserException extends RuntimeException {

    private ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
