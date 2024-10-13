package com.example.message_worker.externalapi.thirdpartiessystem.usersystem.create;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CreateUserResponse implements Serializable {
  private int statusCode;
  private String id;
  private String phoneNumber;
  private String name;
}
