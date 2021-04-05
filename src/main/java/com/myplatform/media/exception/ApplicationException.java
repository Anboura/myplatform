package com.myplatform.media.exception;

import com.myplatform.media.error.AbstractErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class ApplicationException extends RuntimeException {
  private AbstractErrorCode errorCode;
  private HttpStatus httpStatus;
  private Map<String, String> frontParams;
  private String frontCode;

  public ApplicationException(AbstractErrorCode errorCode, HttpStatus httpStatus, String frontCode) {
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
    this.frontCode = frontCode;
  }

  public ApplicationException(
      AbstractErrorCode errorCode, HttpStatus httpStatus, Map<String, String> frontParams, String frontCode) {
    this(errorCode, httpStatus, frontCode);
    this.frontParams = frontParams;
  }
}
