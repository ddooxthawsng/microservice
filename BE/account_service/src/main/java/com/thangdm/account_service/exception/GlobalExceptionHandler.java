package com.thangdm.account_service.exception;

import com.thangdm.common.dto.ApiResponse;
import com.thangdm.common.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

/**
 * Global exception handler for all microservices
 * Handles common exceptions and validation errors
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    /**
     * Handle all uncategorized exceptions
     */
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingUncategorizedException(Exception exception) {
        log.error("Uncategorized exception: ", exception);
        
        ApiResponse response = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ": " + exception.getMessage())
                .build();
        
        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(response);
    }

    /**
     * Handle application-specific exceptions
     */
    @ExceptionHandler(value = AccountException.class)
    ResponseEntity<ApiResponse> handlingAppException(AccountException exception) {
        ErrorCodeAccountService errorCode = exception.getErrorCode();
        
        ApiResponse response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(exception.getMessage())
                .build();
        
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_ENUM_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            // Extract parameters from validation annotation
            // Example: @Length(min = 5, max = 20, message = "INVALID_LENGTH")
            // Returns: {"min": 5, "max": 20, "message": "INVALID_LENGTH"}
            var constraintViolation = exception.getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException e) {
            log.error("Invalid error code enum key: {}", enumKey);
        } catch (Exception e) {
            log.error("Error processing validation exception", e);
        }

        ApiResponse response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes) 
                        ? mapAttribute(errorCode.getMessage(), attributes) 
                        : errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    /**
     * Map validation attributes to error message
     * Replaces placeholders like {min} with actual values
     */
    private String mapAttribute(String message, Map<String, Object> attributes) {
        String result = message;
        
        // Replace {min} placeholder
        if (attributes.containsKey(MIN_ATTRIBUTE)) {
            String minValue = attributes.get(MIN_ATTRIBUTE).toString();
            result = result.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        }
        
        // Add more attribute mappings as needed
        // Example: {max}, {length}, etc.
        
        return result;
    }
}
