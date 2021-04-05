package com.myplatform.media.exception;

public class MediaNotFoundException extends Exception {

  public MediaNotFoundException(String message) {
    super(message);
  }

  public MediaNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
