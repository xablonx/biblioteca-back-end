package com.rennan.biblioteca_back_end.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rennan.biblioteca_back_end.exception.LivroUnavailableException;
import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
  
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFoundException(ResourceNotFoundException exception) {
    return exception.getMessage();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleConstraintViolationException(ConstraintViolationException exception) {
    return exception.getConstraintViolations().stream()
      .map(error -> error.getPropertyPath() + " " + error.getMessage())
      .reduce("", (acc, error) -> acc + error + "\n");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getFieldErrors().stream()
      .map(error -> error.getField() + " " + error.getDefaultMessage())
      .reduce("", (acc, error) -> acc + error + "\n");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public String handleDuplicate(DataIntegrityViolationException exception) {
    return "registro já cadastrado!";
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleIllegalArgumentException(IllegalArgumentException exception) {
    return exception.getMessage();
  }

  @ExceptionHandler(LivroUnavailableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleLivroUnavailableException(LivroUnavailableException exception) {
    return exception.getMessage();
  }
}
