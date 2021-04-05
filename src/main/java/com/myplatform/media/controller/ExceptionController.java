package com.myplatform.media.controller;

import com.myplatform.media.error.ApplicationError;
import com.myplatform.media.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ApplicationError> managerApplicationError(final ApplicationException e) {
    final ApplicationError error =
        new ApplicationError()
            .builder()
            .message(e.getErrorCode().getMessage())
            .errorCode(e.getErrorCode().getCode())
            .frontCode(e.getFrontCode())
            .status(e.getHttpStatus().value())
            .frontParams(e.getFrontParams())
            .build();
    return new ResponseEntity<>(error, e.getHttpStatus());
  }

  //  @ExceptionHandler(MethodArgumentNotValidException.class)
  //  public ResponseEntity<ApplicationError> managerApplicationError(
  //      final MethodArgumentNotValidException e) {
  //    final ApplicationError error = new ApplicationError();
  //    Map<String, String> frontParams = new HashMap<>();
  //    e.getBindingResult().getFieldErrors().forEach(fieldError -> {
  //      frontParams.put(fieldError.getField(),fieldError.getRejectedValue().toString());
  //    });
  //    if(e.getBindingResult().getObjectName().contains("category")) {
  //      error.setFrontCode(CategoryErrorCode.INVALID_CATEGORY_CODE.getCode());
  //    }
  //    error.setFrontParams(frontParams);
  //    error.setStatus(HttpStatus.BAD_REQUEST.value());
  //
  //    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  //  }
}
