package com.example.message_worker.externalapi.thirdpartiessystem.usersystem.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class FilterRequest implements Serializable {
  private String phoneNumber;
}
