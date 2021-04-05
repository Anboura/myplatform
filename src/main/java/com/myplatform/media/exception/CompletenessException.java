package com.myplatform.media.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CompletenessException extends Exception {
  private Map<String, String> completenessErrors = new HashMap<>();

  public CompletenessException(final String message) {
    super(message);
  }

  public CompletenessException(final String message, final Map<String, String> completenessErrors) {
    super(message);
    this.completenessErrors = completenessErrors;
  }
}
