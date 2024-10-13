package com.example.message_service.domain.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class RedisCache implements DisposableBean {

  private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

  private final RedissonClient client;

  public RedisCache() {
    // TODO: read config
    client = Redisson.create();
  }

  @Override
  public void destroy() throws Exception {
    try {
      if (client != null) {
        client.shutdown();
        logger.info("Redisson shutdown success");
      }
    } catch (Exception ex) {
      logger.error("Redisson shutdown error");
    }
  }

  public RedissonClient getClient() {
    return client;
  }
}
