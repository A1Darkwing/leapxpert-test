package com.example.message_service.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for valid phone
 */
public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, String> {

  // https://www.journaldev.com/641/regular-expression-phone-number-validation-in-java
  // https://stackoverflow.com/questions/6028553/regex-allowing-spaces-for-a-phone-number-regex
  public static final String PHONE_PATTERN1 = "^\\+?\\d{2}(?: ?\\d+)*$";
  // 123-4567890
  public static final String PHONE_PATTERN2 = "\\d{3}[-\\.\\s]\\d{3}[\\.\\s]\\d{4}";
  // 123-456-7890 x1234
  // 123-456-7890 ext1234
  public static final String PHONE_PATTERN3 = "\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}";
  // (123)-456-7890
  public static final String PHONE_PATTERN4 = "\\(\\d{3}\\)-\\d{3}-\\d{4}";
  private Pattern pattern;
  private Matcher matcher;

  @Override
  public boolean isValid(String phone, ConstraintValidatorContext context) {
    return (validatePhone(phone));
  }

  private boolean validatePhone(String phone) {
    // Null phone should be covered in NotBlank
    if (phone == null || phone.isEmpty()) {
      return true;
    }
    pattern = Pattern.compile(PHONE_PATTERN1);
    matcher = pattern.matcher(phone);
    if (matcher.matches()) {
      return true;
    } else {
      pattern = Pattern.compile(PHONE_PATTERN2);
      matcher = pattern.matcher(phone);
      if (matcher.matches()) {
        return true;
      } else {
        pattern = Pattern.compile(PHONE_PATTERN3);
        matcher = pattern.matcher(phone);
        if (matcher.matches()) {
          return true;
        } else {
          pattern = Pattern.compile(PHONE_PATTERN4);
          matcher = pattern.matcher(phone);
          if (matcher.matches()) {
            return true;
          } else {
            return false;
          }
        }
      }
    }
  }

  @Override
  public void initialize(ValidPhone arg0) {}
}
