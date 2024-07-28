package com.management.studentstays.App.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EmailResponse {
  private HttpStatus status;
  private String message;
}
