package com.management.studentstays.App.controller;

import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.service.FileService;
import com.management.studentstays.App.service.StudentService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

  @Value("${project.image}")
  private String path;

  @Autowired private FileService fileService;

  @Autowired private StudentService studentService;

  @PostMapping("/student/{studentId}")
  public ResponseEntity<StudentDTO> fileUpload(
      @RequestParam("image") MultipartFile image, @PathVariable Integer studentId) {
    System.out.println("Inside fileUpload");
    System.out.println("image : " + image);
    System.out.println("studentId : " + studentId);

    StudentDTO student = studentService.getStudentById(studentId);
    System.out.println("student : " + student);
    String fileName = null;
    try {
      fileName = this.fileService.uploadImage(path, image);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("fileName : " + fileName);
    student.setProfileImagePath(fileName);
    System.out.println("after student : " + student);
    StudentDTO updatedStudent = studentService.updateStudent(studentId, student);
    System.out.println("-------------------------------");
    System.out.println("updatedStudent : " + updatedStudent);
    return new ResponseEntity<StudentDTO>(updatedStudent, HttpStatus.OK);
  }
}
