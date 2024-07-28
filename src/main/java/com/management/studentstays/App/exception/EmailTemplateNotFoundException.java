package com.management.studentstays.App.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTemplateNotFoundException extends RuntimeException {
  String templateName;

  public EmailTemplateNotFoundException(String templateName) {
    super(String.format("Template not found with template name : %s", templateName));
    this.templateName = templateName;
  }
}
