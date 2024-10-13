package com.example.message_worker.externalapi.client.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

@Service
public class HttpRequester {
  /**
   * similar Requester class
   *
   * @return
   * @throws Exception
   */
  public String sendPost(HttpConfig http, Map<String, String> params) throws Exception {
    RequestConfig.Builder builder = RequestConfig.custom()
                                                 .setSocketTimeout(http.getTimeoutMilliseconds())
                                                 .setConnectTimeout(http.getTimeoutMilliseconds())
                                                 .setConnectionRequestTimeout(http.getTimeoutMilliseconds())
                                                 .setStaleConnectionCheckEnabled(true);
    if (http.isUseProxy()) {
      HttpHost proxy = new HttpHost(http.getProxyHost(), http.getProxyPort());
      builder.setProxy(proxy);
    }
    
    try (CloseableHttpClient httpClient = HttpClients.custom()
                                                     .setDefaultRequestConfig(builder.build())
                                                     .build()) {
      HttpPost httpPost = new HttpPost(http.getUrl());
      httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
      
      List<NameValuePair> nvps = new ArrayList<>();
      Iterator<String> dataKeys = params.keySet().iterator();
      while (dataKeys.hasNext()) {
        String key = dataKeys.next();
        String value = params.get(key);
        nvps.add(new BasicNameValuePair(key, value));
      }
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        if (response.getStatusLine().getStatusCode() != 200) {
          throw new IOException("Failed : HTTP getStatusCode: "
                                + response.getStatusLine().getStatusCode()
                                + " HTTP getReasonPhrase: "
                                + response.getStatusLine().getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        try (InputStream inputStream = entity.getContent();) {
          return IOUtils.toString(inputStream, "UTF-8");
        }
      }
    }
  }

  public HttpResponse<String> sendPostJson(String url, String json) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");

    // Ghi dữ liệu vào output stream
    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = json.getBytes("utf-8");
      os.write(input, 0, input.length);
    }

    // Lấy mã trạng thái và phản hồi
    int statusCode = connection.getResponseCode();
    String responseBody = ""; // Đọc body từ input stream

    if (statusCode >= 200 && statusCode < 300) {
      try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
          response.append(responseLine.trim());
        }
        responseBody = response.toString();
      }
    } else {
      // Xử lý trường hợp lỗi
      // Đọc từ connection.getErrorStream() nếu cần
    }

    return new HttpResponse<>(statusCode, responseBody);
  }

  public String sendPostJson(HttpConfig http, String jsonContent, String contentType) throws Exception {
    RequestConfig.Builder builder = RequestConfig.custom()
                                                 .setSocketTimeout(http.getTimeoutMilliseconds())
                                                 .setConnectTimeout(http.getTimeoutMilliseconds())
                                                 .setConnectionRequestTimeout(http.getTimeoutMilliseconds())
                                                 .setStaleConnectionCheckEnabled(true);
    if (http.isUseProxy()) {
      HttpHost proxy = new HttpHost(http.getProxyHost(), http.getProxyPort());
      builder.setProxy(proxy);
    }

    try (CloseableHttpClient httpClient = HttpClients.custom()
                                                     .setDefaultRequestConfig(builder.build())
                                                     .build()) {
      HttpPost httpPost = new HttpPost(http.getUrl());
      StringEntity input = new StringEntity(jsonContent, "UTF-8");
      input.setContentType(contentType);
      httpPost.setEntity(input);

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        if (response.getStatusLine().getStatusCode() != 200) {
          throw new IOException(
            "Failed : HTTP getStatusCode: " + response.getStatusLine().getStatusCode() + " HTTP getReasonPhrase: " +
            response.getStatusLine().getReasonPhrase());
        } else {
          try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            StringBuilder strBuilder = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
              strBuilder.append(output);
            }

            return strBuilder.toString();
          } catch (Throwable e) {
            throw new Exception(e);
          }
        }
      } catch (Throwable e) {
        throw new Exception(e);
      }
    } catch (Throwable e) {
      throw new Exception(e);
    }
  }
}
