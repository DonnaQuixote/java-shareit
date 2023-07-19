package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.time.DateTimeException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "ru.practicum.shareit")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleNotFound(final NoSuchElementException e) {
        return Map.of("ОШИБКА", "id пользователя или вещи некорректен");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleNoHeader(final MissingRequestHeaderException e) {
        return Map.of("ОШИБКА", "id пользователя не обнаружен");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleValidation(final ValidationException e) {
        return Map.of("ОШИБКА", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleWrongDateTime(final DateTimeException e) {
        return Map.of("ОШИБКА", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleChangeStatusWhenApproved(final UnsupportedOperationException e) {
        return Map.of("ОШИБКА", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleBadEnum(final IllegalArgumentException e) {
        return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
    }
}