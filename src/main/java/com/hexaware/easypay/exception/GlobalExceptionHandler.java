package com.hexaware.easypay.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResource(
            DuplicateResourceException ex) {

        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<String> errors =
                ex.getBindingResult()
                  .getFieldErrors()
                  .stream()
                  .map(error -> error.getDefaultMessage())
                  .toList();

        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobal(Exception ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(
            DepartmentDeletionException.class)
    public ResponseEntity<String>
    handleDepartmentDeletion(
            DepartmentDeletionException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(
            InvalidLeaveRequestException.class)
    public ResponseEntity<String>
    handleInvalidLeaveRequest(
            InvalidLeaveRequestException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(
            InvalidPayrollStateException.class)
    public ResponseEntity<String>
    handleInvalidPayrollState(
            InvalidPayrollStateException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}