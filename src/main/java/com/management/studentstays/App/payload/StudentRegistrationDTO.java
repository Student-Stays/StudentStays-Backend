package com.management.studentstays.App.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRegistrationDTO {
  @NotBlank private String name;

  @NotBlank private String username;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;
}
