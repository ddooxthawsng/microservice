package com.thangdm.account_service.exception;

import lombok.Data;

@Data
public class AccountException extends RuntimeException {

    private ErrorCodeAccountService errorCode;

    public AccountException(ErrorCodeAccountService errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
