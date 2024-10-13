package com.example.message_worker.externalapi.thirdpartiessystem.usersystem.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FilterResponse implements Serializable {
  private int statusCode;
  private String id;
  private String phoneNumber;
  private String name;
}
