package com.example.message_service.exception;

import org.apache.commons.collections4.CollectionUtils;

public class ValidationException extends CoreException {
  private static final long serialVersionUID = -4841921009013208283L;

  private ApplicationErrorContainer errContainer;

  public ValidationException(ApplicationErrorContainer errContainer) {
    super(CoreException.VALIDATION_EXCEPTION);
    this.errContainer = errContainer;
  }

  public ApplicationErrorContainer getApplicationErrorContainer() {
    return errContainer;
  }

  public void setErrContainer(ApplicationErrorContainer errContainer) {
    this.errContainer = errContainer;
  }

  @Override
  public String getCode() {
    super.code =
        CollectionUtils.union(errContainer.getSystems(), errContainer.getValidations()).toString();
    return code;
  }
}
