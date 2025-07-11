package com.example.userapi.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  record ApiError(
    LocalDateTime ts,
    int status,
    String message
  ) {}

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> badReqeust(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult()
                   .getFieldErrors().stream()
                   .map(e -> e.getDefaultMessage())
                   .collect(Collectors.joining(", "));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(LocalDateTime.now(), 400, msg));
  }
  
}
