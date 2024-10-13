package com.example.message_worker.externalapi.client.http;

import java.io.Serializable;
import lombok.Data;

@Data
public class HttpConfig implements Serializable {
  private String url;
  private int timeoutMilliseconds = 20000;
  private boolean useProxy = false;
  private String proxyHost;
  private int proxyPort = 0;
}

