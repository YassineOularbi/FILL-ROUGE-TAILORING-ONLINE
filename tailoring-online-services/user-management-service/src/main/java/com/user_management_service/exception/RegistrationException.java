package com.user_management_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class RegistrationException extends RuntimeException {

  private final List<String> details;

  public RegistrationException(String message, List<String> details) {
    super(message);
    this.details = details;
  }

}
