package com.management.studentstays.App.payload;

import lombok.Data;

@Data
public class RegisterStudentDTO {
  private String studentName;

  private String emailAddress;

  private String aadharCardNumber;

  private String password;
}
