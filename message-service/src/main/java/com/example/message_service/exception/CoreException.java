package com.example.message_service.exception;

public class CoreException extends RuntimeException {

  public static final String SYSTEM_EXCEPTION = "System exception";
  public static final String VALIDATION_EXCEPTION = "Validation exception";
  private static final long serialVersionUID = -754917469401803932L;
  protected String type;
  protected String code;
  protected String message;
  protected Exception exception;

  public CoreException(String type, String code, String message, Exception exception) {
    this.type = type;
    this.code = code;
    this.message = message;
    this.exception = exception;
  }

  public CoreException(String type, String code, Exception exception) {
    this.type = type;
    this.code = code;
    this.exception = exception;
  }

  public CoreException(String type, String code) {
    this.type = type;
    this.code = code;
  }

  public CoreException(String type) {
    this.type = type;
  }

  public String getCode() {
    return code;
  }

  public Exception getException() {
    return exception;
  }

  public String getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
