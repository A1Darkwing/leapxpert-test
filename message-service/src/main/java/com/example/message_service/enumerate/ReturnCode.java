package com.example.message_service.enumerate;

public enum ReturnCode {
  SUCCESS(1, "success!");

  // TODO use return code
  private Integer returnCode;
  private String returnMessage;

  ReturnCode(Integer returnCode, String returnMessage) {
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
  }

  public Integer getReturnCode() {
    return returnCode;
  }

  public String getReturnMessage() {
    return returnMessage;
  }
}
