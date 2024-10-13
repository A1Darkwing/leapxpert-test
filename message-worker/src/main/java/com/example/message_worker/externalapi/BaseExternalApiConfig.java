package com.example.message_worker.externalapi;

import com.example.message_worker.externalapi.client.http.HttpConfig;
import lombok.Data;

@Data
public abstract class BaseExternalApiConfig {

  private HttpConfig http;
  private boolean testing;
  private String testData;

}
