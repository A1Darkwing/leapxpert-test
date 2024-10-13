package com.example.message_service.exception;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class ApplicationErrorContainer {

  private List<String> validations = new ArrayList<>();
  private List<String> systems = new ArrayList<>();

  public ApplicationErrorContainer() {}

  public void addValidationErrors(String... errs) {
    CollectionUtils.addAll(validations, errs);
  }

  public void addSystemErrors(String... errs) {
    CollectionUtils.addAll(systems, errs);
  }

  public List<String> getValidations() {
    return validations;
  }

  public List<String> getSystems() {
    return systems;
  }
}
