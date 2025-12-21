package com.thangdm.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    // Common error codes (1000-1999)
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ENUM_KEY(9998, "Invalid enum key", HttpStatus.BAD_REQUEST),
    
    // Authentication & Authorization (1000-1099)
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(1002, "Invalid username or password", HttpStatus.BAD_REQUEST),
    
    // User related (1100-1199)
    USER_EXISTED(1100, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOTFOUND(1101, "User not found", HttpStatus.NOT_FOUND),
    USER_USERNAME_ERR(1102, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_ERR(1103, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1104, "Age must be greater than {min}", HttpStatus.BAD_REQUEST),
    
    // Validation errors (1200-1299)
    INVALID_INPUT(1200, "Invalid input", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(1201, "Missing required field", HttpStatus.BAD_REQUEST),
    
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
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
