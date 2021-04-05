package com.myplatform.media.exception;

import lombok.Getter;
import lombok.Setter;

public class CategoryNotFoundException extends Exception {
  @Getter @Setter private String categoryLabel;
  @Getter @Setter private String categoryId;

  public CategoryNotFoundException(String message) {
    super(message);
  }

  public CategoryNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
