package com.example.purchase_prepaid_data_common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.lang.reflect.Type;

public class JsonUtil {
  private static final Gson gson = new Gson();

  public static String toJsonString(Object obj) {
    return gson.toJson(obj);
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    return gson.fromJson(json, clazz);
  }

  public static <T> T toObject(String json, Type type) {
    return gson.fromJson(json, type);
  }

  public static <T> T toObject(String json) {
    return gson.fromJson(json, (new TypeToken<T>() {}).getType());
  }

  public static <T> T toObject(Reader reader) {
    return gson.fromJson(reader, (new TypeToken<T>() {}).getType());
  }

}
