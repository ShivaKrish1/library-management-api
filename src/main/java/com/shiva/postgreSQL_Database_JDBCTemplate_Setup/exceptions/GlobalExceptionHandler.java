package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/*Catches exceptions thrown anywhere in the app's controllers and converts
  them into proper HTTP responses instead of letting them fall through as
  an unhandled 500.*/

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Triggered by @Valid failing on a @RequestBody (e.g. blank name, malformed email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "One or more fields are invalid",
                details
        );
        return ResponseEntity.badRequest().body(error);
    }

    // Triggered by DB constraint violations, e.g. the unique constraint on UserEntity.email
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "The request conflicts with existing data (e.g. a duplicate email)"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}