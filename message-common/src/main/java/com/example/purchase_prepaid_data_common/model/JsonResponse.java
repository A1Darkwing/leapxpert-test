package com.example.purchase_prepaid_data_common.model;

public class JsonResponse<T> {
  public static final String STATUS_SUCCESS = "SUCCESS";
  public static final String STATUS_FAIL = "FAIL";
  public static final String STATUS_UNKNOWN = "UNKNOWN";

  private String status;
  private T result;

  protected JsonResponse(String status, T result) {
    this.status = status;
    this.result = result;
  }

  public static <T> JsonResponse<T> accept(T t) {
    return new JsonResponse<T>(STATUS_SUCCESS, t);
  }

  public static <T> JsonResponse<T> reject(T t) {
    return new JsonResponse<T>(STATUS_FAIL, t);
  }

  public static JsonResponse<String> unknown(String id) {
    return new JsonResponse<String>(STATUS_UNKNOWN, id);
  }

  public String getStatus() {
    return status;
  }

  public T getResult() {
    return result;
  }
}
