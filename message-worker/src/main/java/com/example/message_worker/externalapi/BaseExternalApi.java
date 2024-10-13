package com.example.message_worker.externalapi;

import com.example.message_worker.externalapi.client.http.HttpRequester;
import com.example.message_worker.externalapi.client.http.HttpResponse;
import com.example.message_worker.util.GsonUtil;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class to implement the external api
 *
 * @param <T> entity to build the request
 * @param <R> entity to build the response
 * @param <C> configuration of the external api
 */
public abstract class BaseExternalApi<T, R , C extends BaseExternalApiConfig> {
  private static final Logger logger = LogManager.getLogger(BaseExternalApi.class);

  @Autowired private HttpRequester requester;

  /**
   * @param entity </T>
   * @return response of type R
   */
  public R call(T entity) {
    String prefixLog = getPrefixLog();
    C config = getConfig();
    logger.info("{}# call api with entity={}", prefixLog, GsonUtil.toJsonString(entity));

    R response = null;
    Map<String, String> requestParams = null;
    long start = System.currentTimeMillis();
    try {
      String jsonResponse;
      int statusCode;
      requestParams = buildRequest(entity);

      if (config.isTesting()) {
        jsonResponse = config.getTestData();
        statusCode = 200;
      } else {
        HttpResponse<String> httpResponse = requester.sendPostJson(config.getHttp().getUrl(), GsonUtil.toJsonString(requestParams));
        statusCode = httpResponse.getStatusCode();
        jsonResponse = httpResponse.getBody();

        // Gọi phương thức xử lý mã trạng thái
        handleHttpStatusCode(statusCode, prefixLog);
      }

      logger.info("{}# raw response={}", prefixLog, jsonResponse);
      response = parseResponse(jsonResponse, statusCode);
    } catch (Exception e) {
      logger.error("{}#ERROR entity={}", prefixLog, GsonUtil.toJsonString(entity), e);
    } finally {
      logger.info(
              "{}# elapse={} request={} response={}",
              prefixLog,
              (System.currentTimeMillis() - start),
              GsonUtil.toJsonString(requestParams),
              GsonUtil.toJsonString(response));
    }
    return response;
  }


  protected abstract String getPrefixLog();

  protected abstract C getConfig();

  protected abstract Map<String, String> buildRequest(T entity);

  protected abstract R parseResponse(String jsonResponse, int statusCode);

  protected abstract void handleHttpStatusCode(int statusCode, String prefixLog);
}
