package com.management.studentstays.App.service;

import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.payload.RegisterStudentDTO;
import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.payload.StudentDTOWithImage;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

  RegisterStudentDTO registerNewUser(RegisterStudentDTO studentDTO);

  StudentDTO saveStudent(StudentDTO student);

  StudentDTO saveStudentWithImage(StudentDTOWithImage studentdto);

  StudentDTO updateStudent(int studentId, StudentDTO student);

  StudentDTO updateStudent2(
      int studentId,
      StudentDTO student,
      MultipartFile profilePicture,
      MultipartFile aadharCardPicture);

  List<StudentDTO> getAllStudents();

  StudentDTO getStudentById(int id);

  boolean isAadharCardNumberPresent(String aadharCardNumber);

  // new methods for paymentServiceImpl
  Student getStudent(int id);

  Student saveStudentFromPaymentServiceImpl(Student student);

  void deleteStudentById(int id);

  List<StudentDTO> getStudentsByRoomId(Integer roomId);

  List<StudentDTO> getStudentsByFloorNumber(Integer roomId);

  List<StudentDTO> getStudentsPaymentStatus();

  void saveDefaultRolesOnStartup();
}
