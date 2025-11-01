package com.rennan.biblioteca_back_end.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LivroUnavailableException extends RuntimeException {

  public LivroUnavailableException(String message) {
    super(message);
  }
}
