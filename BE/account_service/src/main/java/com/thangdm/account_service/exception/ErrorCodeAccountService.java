package com.thangdm.account_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCodeAccountService {
    USER_EXISTED(1000, "User exist", HttpStatus.BAD_REQUEST),
    USER_NOTFOUND(1001, "User not found", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1002, "Invalid username or password", HttpStatus.BAD_REQUEST),
    USER_USERNAME_ERR(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_ERR(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1006, "Tuổi phải lớn hơn {min}", HttpStatus.BAD_REQUEST),
    ROLE_NOTFOUND(1007, "Role not found", HttpStatus.BAD_REQUEST),
    PERMISSION_NOTFOUND(1008, "Permission not found", HttpStatus.BAD_REQUEST),
    INVALID_ENUM_KEY(9998, "INVALID_VALID_KEY", HttpStatus.BAD_GATEWAY),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private final HttpStatusCode statusCode;

    ErrorCodeAccountService(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode =statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
