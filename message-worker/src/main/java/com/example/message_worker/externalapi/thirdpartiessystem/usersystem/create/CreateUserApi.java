package com.example.message_worker.externalapi.thirdpartiessystem.usersystem.create;

import com.example.message_worker.externalapi.BaseExternalApi;
import com.example.message_worker.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CreateUserApi extends BaseExternalApi<CreateUserRequest, CreateUserResponse, CreateUserConfig> {

  private static final Logger logger = LoggerFactory.getLogger(CreateUserApi.class);

  @Autowired CreateUserConfig config;

  @Override
  protected String getPrefixLog() {
    return "userSystem.CreateUser";
  }

  @Override
  protected CreateUserConfig getConfig() {
    return config;
  }

  @Override
  protected Map<String, String> buildRequest(CreateUserRequest entity) {
    final Map<String, String> params = new LinkedHashMap<>();
    params.put("phoneNumber", String.valueOf(entity.getPhoneNumber()));
    return params;
  }

  @Override
  protected CreateUserResponse parseResponse(String jsonResponse, int statusCode) {
    CreateUserResponse response = GsonUtil.fromJsonString(jsonResponse, CreateUserResponse.class);
    response.setStatusCode(statusCode);
    return response;
  }

  @Override
  protected void handleHttpStatusCode(int statusCode, String prefixLog) {
    switch (statusCode) {
      case 200:
        logger.info("{}# Request successful", prefixLog);
        break;
      case 400:
        logger.warn("{}# Bad Request - The request was invalid", prefixLog);
        break;
      case 401:
        logger.warn("{}# Unauthorized - Authentication failed", prefixLog);
        break;
      case 403:
        logger.warn("{}# Forbidden - Access denied", prefixLog);
        break;
      case 404:
        logger.warn("{}# Not Found - The requested resource was not found", prefixLog);
        break;
      case 500:
        logger.error("{}# Internal Server Error - Something went wrong on the server", prefixLog);
        break;
      default:
        logger.error("{}# Unexpected response code: {}", prefixLog, statusCode);
        break;
    }
  }
}
