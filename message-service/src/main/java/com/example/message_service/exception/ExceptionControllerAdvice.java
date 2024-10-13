package com.example.message_service.exception;

import com.example.purchase_prepaid_data_common.model.JsonResponse;
import com.example.message_service.domain.genid.GenTransId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  @Autowired private GenTransId genTransId;

  @ExceptionHandler(value = {Throwable.class})
  protected ResponseEntity<Object> handleConflict(Exception ex) {
    if (ex instanceof ValidationException) {
      ApplicationErrorContainer container =
          ((ValidationException) ex).getApplicationErrorContainer();
      return new ResponseEntity<>(JsonResponse.reject(container), HttpStatus.OK);
    } else {
      String id = genTransId.nextId();
      logger.error("Error Id: " + id, ex);
      if (ex instanceof CoreException) {
        logger.error("Error Id: " + id, ((CoreException) ex).getException());
      }
      return new ResponseEntity<Object>(JsonResponse.unknown(id), HttpStatus.OK);
    }
  }
}
