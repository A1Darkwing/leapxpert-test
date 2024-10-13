package com.example.message_worker.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;

public class GsonUtil {
  private static final Gson gson;

  static {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    gson = gsonBuilder.disableHtmlEscaping().create();
  }

  public GsonUtil() {}

  public static String toJsonString(Object obj) {
    return gson.toJson(obj);
  }

  public static <T> T fromJsonString(String sJson, Class<T> t) {
    return gson.fromJson(sJson, t);
  }

  public static <T> T fromJsonString(String json, Type typeOfT) throws JsonSyntaxException {
    return gson.fromJson(json, typeOfT);
  }
}
