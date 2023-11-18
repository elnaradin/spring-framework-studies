package com.example.tasktracker.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> notFound(EntityNotFoundException e) {
        log.warn(e.getMessage());
        return Mono.just(new ErrorResponse(e.getMessage()))
                .map((ErrorResponse response) -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(response));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationExceptions(
            ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }
}
