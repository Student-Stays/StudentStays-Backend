package com.management.studentstays.App.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailDTO {
  private String to;
  private String name;
  private String subject;
  private String text;
}
