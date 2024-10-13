package com.example.message_service.validation;

import com.example.message_service.exception.ApplicationErrorContainer;
import com.example.message_service.exception.ValidationException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public final class ValidationUtils {
  public static final String TEXT_LENGTH = "TextLength";
  public static final String NAME_VALID = "NameValid";
  public static final String TEXT_PATTERN = "TextPattern";
  public static final String LIST_SIZE = "ListSize";
  public static final String DATETIME_MAX = "DatetimeMax";
  public static final String DATETIME_MIN = "DatetimeMin";

  public static final String VALUE_UNIQUE = "ValueUnique";
  public static final String VALIDATE_EXIST_PREFIX = "exist";
  public static final String VALID_PHONE = "ValidPhone";

  private static final String AMOUNT_REGEX = "(-|\\+)?\\d+";
  private static final String UNIT_REGEX = "d|m|y";

  private ValidationUtils() {}

  public static void handleValidationResult(Errors errors) {
    if (errors.hasErrors()) {
      ApplicationErrorContainer container = new ApplicationErrorContainer();
      for (ObjectError error : errors.getAllErrors()) {
        container.addValidationErrors(error.getDefaultMessage());
      }
      throw new ValidationException(container);
    }
  }

  public static void handleValidationResult(String error) {
    ApplicationErrorContainer container = new ApplicationErrorContainer();
    container.addValidationErrors(error);
    throw new ValidationException(container);
  }

  /**
   * get first error message
   *
   * @param errors
   * @return
   */
  public static String getFirstErrorMessage(Errors errors) {
    if (errors.hasErrors()) {
      return errors.getAllErrors().get(0).getDefaultMessage();
    }
    return "";
  }
}
