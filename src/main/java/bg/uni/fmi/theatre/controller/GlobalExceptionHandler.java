package bg.uni.fmi.theatre.controller;

import bg.uni.fmi.theatre.exception.NotFoundException;
import bg.uni.fmi.theatre.dto.ErrorResponse;
import bg.uni.fmi.theatre.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final int NOT_FOUND_CODE = 404;
    private static final int VALIDATION_ERROR_CODE = 400;
    private static final int GENERAL_SERVER_ERROR_CODE = 500;

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(NOT_FOUND_CODE, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(ValidationException ex, HttpServletRequest request) {
        return new ErrorResponse(VALIDATION_ERROR_CODE, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));
        return new ErrorResponse(VALIDATION_ERROR_CODE, message, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralException(Exception ex, HttpServletRequest request) {
        return new ErrorResponse(GENERAL_SERVER_ERROR_CODE, "An unexpected error occurred: " + ex.getMessage(), request.getRequestURI());
    }
}
