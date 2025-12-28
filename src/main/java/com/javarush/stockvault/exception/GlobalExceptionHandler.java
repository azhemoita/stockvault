package com.javarush.stockvault.exception;

import com.javarush.stockvault.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation failed", errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiErrorResponse handleBadCredentials(BadCredentialsException ex) {
        return new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Invalid email or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ApiErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden", "Access denied");
    }

    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleGeneric(Exception ex) {
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage());
    }
}
