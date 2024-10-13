package com.example.message_service.domain.genid;

import com.example.purchase_prepaid_data_common.util.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenTransId {

  @Value("${app.nodeId}")
  private int nodeId;

  private Snowflake engine = new Snowflake(nodeId);

  public String nextId() {
    return String.valueOf(engine.nextId());
  }
}
