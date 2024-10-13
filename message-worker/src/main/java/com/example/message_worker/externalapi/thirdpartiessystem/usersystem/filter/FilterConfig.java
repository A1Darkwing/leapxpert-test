package com.example.message_worker.externalapi.thirdpartiessystem.usersystem.filter;

import com.example.message_worker.externalapi.BaseExternalApiConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "third-parties-config.user.filters")
@Data
public class FilterConfig extends BaseExternalApiConfig {

}
