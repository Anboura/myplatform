package com.myplatform.media.exception;

public class MediaTypeNotFoundException extends Exception {
  public MediaTypeNotFoundException(String message) {
    super(message);
  }

  public MediaTypeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
