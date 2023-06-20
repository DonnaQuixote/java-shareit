package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
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
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,String> handleEmailDuplication(final ValidationException e) {
        return Map.of("ОШИБКА", "Пользователь с данным email уже существует");
    }
}