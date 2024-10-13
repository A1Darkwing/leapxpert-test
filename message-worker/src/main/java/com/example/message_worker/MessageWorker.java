package com.example.message_worker;

import static org.springframework.boot.WebApplicationType.NONE;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MessageWorker {

  public static void main(String[] args) {
    new SpringApplicationBuilder(MessageWorker.class).web(NONE).run(args);
  }
}
