package com.example.purchase_prepaid_data_common.enumerate;

import java.util.HashMap;
import java.util.Map;

public enum MessageStatus {
  CREATED("CREATED"),
  PROCESSING("PROCESSING"),
  SUCCESS("SUCCESS"),
  FAIL("FAIL");

  private static final Map<String, MessageStatus> returnMap = new HashMap<String, MessageStatus>();

  static {
    for (MessageStatus enums : MessageStatus.values()) {
      returnMap.put(enums.value, enums);
    }
  }

  private String value;

  MessageStatus(String value) {
    this.value = value;
  }

  public static MessageStatus fromString(String status) {
    return returnMap.get(status);
  }

  public String getValue() {
    return this.value;
  }


}
