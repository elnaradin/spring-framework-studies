package com.example.tasktracker.controller;

import com.example.tasktracker.dto.ErrorResponse;
import com.example.tasktracker.exception.DuplicateEntryException;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.exception.UserNotAuthenticatedException;
import com.example.tasktracker.exception.UserNotAuthorizedException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler({ConstraintViolationException.class, DuplicateEntryException.class})
    public ResponseEntity<ErrorResponse> handleBadRequests(Exception e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        String errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }

    @ExceptionHandler({UsernameNotFoundException.class, UserNotAuthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(Exception e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UserNotAuthenticatedException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

}
