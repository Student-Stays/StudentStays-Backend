package com.management.studentstays.App.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.impl.RentPaymentSchedule;
import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.payload.StudentDTOWithImage;
import com.management.studentstays.App.payload.StudentResponseDTO;
import com.management.studentstays.App.repo.StudentRepo;
import com.management.studentstays.App.service.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

  @Autowired private StudentService service;

  @Autowired private StudentRepo repo;

  @Autowired private RentPaymentSchedule rentPaymentSchedule;

  @Autowired private ModelMapper modelMapper;

  @PostMapping("/student/admin/save")
  public ResponseEntity<StudentDTO> addStudent(@Valid @RequestBody StudentDTO studentdto) {
    StudentDTO createdStudent = service.saveStudent(studentdto);
    return new ResponseEntity<StudentDTO>(createdStudent, HttpStatus.CREATED);
  }

  @PostMapping("/student/user/save")
  public ResponseEntity<StudentDTO> addStudentWithImage(
      @RequestParam("profileImage") MultipartFile profilePicture,
      @RequestParam("aadharCardImage") MultipartFile AadharCardPicture,
      @RequestParam("studentDTO") String studentDTOJson) {
    ObjectMapper objectMapper = new ObjectMapper();
    StudentDTO studentDTO = null;
    try {
      studentDTO = objectMapper.readValue(studentDTOJson, StudentDTO.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    StudentDTOWithImage studentDTOWithImage =
        new StudentDTOWithImage(studentDTO, profilePicture, AadharCardPicture);
    StudentDTO savedStudent = service.saveStudentWithImage(studentDTOWithImage);
    return new ResponseEntity<StudentDTO>(savedStudent, HttpStatus.CREATED);
  }

  @PostMapping("/student/update/{studentID}")
  public ResponseEntity<StudentDTO> updateStudent(
      @RequestParam("studentDTO") String studentdto,
      @PathVariable Integer studentID,
      @RequestParam(value = "profileImage", required = false) MultipartFile profilePicture,
      @RequestParam(value = "aadharCardImage", required = false) MultipartFile aadharCardPicture) {
    System.out.println("Inside updateStudent Controller");
    System.out.println("Studentdto : " + studentdto);
    System.out.println("StudentID : " + studentID);
    System.out.println("profilePicture : " + profilePicture);
    System.out.println("aadharCardPicture : " + aadharCardPicture);

    ObjectMapper objectMapper = new ObjectMapper();
    StudentDTO studentDTO = null;
    try {
      studentDTO = objectMapper.readValue(studentdto, StudentDTO.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    System.out.println("studentDTO After parsing : " + studentDTO);

    StudentDTO updatedStudent =
        service.updateStudent2(studentID, studentDTO, profilePicture, aadharCardPicture);
    return new ResponseEntity<StudentDTO>(updatedStudent, HttpStatus.CREATED);
  }

  @GetMapping("/students")
  public ResponseEntity<List<StudentDTO>> fetchAllStudent() {
    List<StudentDTO> listOfstudent = service.getAllStudents();
    return new ResponseEntity<List<StudentDTO>>(listOfstudent, HttpStatus.OK);
  }

  @GetMapping("/student/{studentID}")
  public ResponseEntity<StudentResponseDTO> fetchByStudentID(@PathVariable Integer studentID) {
    System.out.println("----- Inside fetchByStudentID -----");
    StudentDTO student = service.getStudentById(studentID);
    System.out.println("student BEFORE payment cal: " + student.getPayments());

    StudentResponseDTO studentResponse = modelMapper.map(student, StudentResponseDTO.class);
    return new ResponseEntity<StudentResponseDTO>(studentResponse, HttpStatus.OK);
  }

  @GetMapping("/student/email/{email}")
  public ResponseEntity<StudentDTO> fetchStudentbyEmail(@PathVariable String email) {
    System.out.println("Inside fetchStudentbyEmail");
    Student student = repo.findByEmail(email);
    if (student == null) return null;
    System.out.println("Student : " + student);
    StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);
    System.out.println("StudentDTO : " + studentDTO);
    return new ResponseEntity<StudentDTO>(studentDTO, HttpStatus.OK);
  }

  @GetMapping("/student/roomNumber/{roomNumber}")
  public ResponseEntity<List<StudentDTO>> fetchStudentsByRoomNumber(
      @PathVariable Integer roomNumber) {
    List<StudentDTO> listOfstudent = service.getStudentsByRoomId(roomNumber);
    return new ResponseEntity<List<StudentDTO>>(listOfstudent, HttpStatus.OK);
  }

  @GetMapping("/student/notPaidStudents")
  public ResponseEntity<List<StudentDTO>> fetchStudentWhoHasNotDonePayment() {
    List<StudentDTO> result = service.getStudentsPaymentStatus();
    return new ResponseEntity<List<StudentDTO>>(result, HttpStatus.OK);
  }

  @GetMapping("/student/floorNumber/{floorNumber}")
  public ResponseEntity<List<StudentDTO>> fetchStudentsByFloorNumber(
      @PathVariable Integer floorNumber) {
    List<StudentDTO> listOfstudent = service.getStudentsByFloorNumber(floorNumber);
    return new ResponseEntity<List<StudentDTO>>(listOfstudent, HttpStatus.OK);
  }

  @GetMapping("/student/getTest/{id}")
  public ResponseEntity<Student> getTest(@PathVariable int id) {
    Student student = service.getStudent(id);
    return new ResponseEntity<Student>(student, HttpStatus.OK);
  }

  @PostMapping("/student/isAadharCardPresent/{aadharCardNumber}")
  public ResponseEntity<Boolean> checkAadharCardInDB(@PathVariable String aadharCardNumber) {
    boolean isPresent = service.isAadharCardNumberPresent(aadharCardNumber);
    return new ResponseEntity<Boolean>(isPresent, HttpStatus.OK);
  }
}
