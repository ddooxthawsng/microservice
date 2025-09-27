package com.thangdm.auth_service.exception;

import com.thangdm.auth_service.dto.request.ApiResponse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTES = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingUncategorizedException(RuntimeException exception) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ":" + exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = UserException.class)
    ResponseEntity<ApiResponse> handlingUserException(UserException exception) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(exception.getErrorCode().getCode());
        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


    //Valid @validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_ENUM_KEY;

        Map<String,Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            //Xử lý lấy ra các param truyền vào annotation
            var constraintViolation = exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        } catch (Exception e) {

        }

        ApiResponse response = new ApiResponse<>();

        response.setCode(errorCode.getCode());
        response.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(),attributes) : errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    private String mapAttribute(String message, Map<String,Object> attributes) {
        String minValue = attributes.get(MIN_ATTRIBUTES).toString();

        return message.replace("{"+MIN_ATTRIBUTES+"}",minValue);

    }
}
