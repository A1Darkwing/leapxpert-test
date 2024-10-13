package com.example.message_worker.externalapi.thirdpartiessystem.usersystem.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class CreateUserRequest implements Serializable {
  private String phoneNumber;
  private String name;
}
