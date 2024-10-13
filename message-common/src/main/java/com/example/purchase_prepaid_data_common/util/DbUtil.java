package com.example.purchase_prepaid_data_common.util;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbUtil {
  private static final SimpleDateFormat DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyyMMdd");
  private static final SimpleDateFormat DATE_FORMAT_YYYY_MM = new SimpleDateFormat("yyyyMM");
  private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public static String convertTime2yyyyMMdd(long timestamp) {
    if (timestamp == 0) {
      return "";
    }
    try {
      return DATE_FORMAT_YYYY_MM_DD.format(new Date(timestamp));
    } catch (Exception ex) {
      return "";
    }
  }

  public static String convertTime2yyyyMM(long timestamp) {
    if (timestamp == 0) {
      return "";
    }
    try {
      return DATE_FORMAT_YYYY_MM.format(new Date(timestamp));
    } catch (Exception ex) {
      return "";
    }
  }


  public static String convertTime2DBDateFormat(long timestamp) {
    if (timestamp == 0) {
      return "";
    }
    try {
      return DB_DATE_FORMAT.format(new Date(timestamp));
    } catch (Exception ex) {
      return "";
    }
  }

  public static String truncateToLength(String longString, int maxLength) {
    if (longString == null) {
      return "";
    } else {
      return longString.length() <= maxLength ? longString : longString.substring(0, maxLength);
    }
  }

  public static long getTimeMs(ResultSet rs, String fieldName) {
    try {
      return rs.getDate(fieldName).getTime();
    } catch (Exception e) {
      return 0;
    }
  }
}
