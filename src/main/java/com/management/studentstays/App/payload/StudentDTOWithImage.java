package com.management.studentstays.App.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StudentDTOWithImage {
  private StudentDTO studentDTO;
  private MultipartFile profilePicture;
  private MultipartFile aadharCardPicture;

  public StudentDTOWithImage(
      StudentDTO studentDTO, MultipartFile profilePicture, MultipartFile aadharCardPicture) {
    this.studentDTO = studentDTO;
    this.profilePicture = profilePicture;
    this.aadharCardPicture = aadharCardPicture;
  }
}
