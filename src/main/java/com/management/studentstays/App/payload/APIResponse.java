package com.management.studentstays.App.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse {
  String message;
  boolean success;

  public APIResponse(String message, boolean success) {
    super();
    this.message = message;
    this.success = success;
  }
}
