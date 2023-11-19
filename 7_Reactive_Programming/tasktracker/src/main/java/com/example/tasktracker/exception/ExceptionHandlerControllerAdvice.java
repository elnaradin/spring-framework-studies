package com.example.tasktracker.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(e.getMessage());
        return Mono.just(new ErrorResponse(e.getMessage()))
                .map((ErrorResponse response) -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(response));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        String errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }
}
