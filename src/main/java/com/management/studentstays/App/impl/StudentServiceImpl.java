package com.management.studentstays.App.impl;

import com.management.studentstays.App.config.AppConstants;
import com.management.studentstays.App.entity.Role;
import com.management.studentstays.App.entity.Room;
import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.exception.APIException;
import com.management.studentstays.App.exception.ResourceNotFoundException;
import com.management.studentstays.App.payload.RegisterStudentDTO;
import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.payload.StudentDTOWithImage;
import com.management.studentstays.App.repo.RoleRepo;
import com.management.studentstays.App.repo.RoomRepo;
import com.management.studentstays.App.repo.StudentRepo;
import com.management.studentstays.App.service.FileService;
import com.management.studentstays.App.service.StudentService;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentServiceImpl implements StudentService {

  @Autowired private StudentRepo studentRepo;

  @Autowired private RoomRepo roomRepo;

  @Autowired private ModelMapper modelMapper;

  @Autowired private FileService fileService;

  @Autowired private RoleRepo roleRepo;

  @Autowired private PasswordEncoder passwordEncoder;

  @Value("${project.image}")
  private String path;

  @Override
  @Transactional
  public RegisterStudentDTO registerNewUser(RegisterStudentDTO studentDTO) {
    Student student = new Student();

    student.setName(studentDTO.getStudentName());
    student.setEmail(studentDTO.getEmailAddress());
    String encodedPassword = passwordEncoder.encode(studentDTO.getPassword());
    student.setPassword(encodedPassword);

    student.setAadharCardNumber(studentDTO.getAadharCardNumber());

    // handle when no role is present in the db
    Role role = roleRepo.findById(AppConstants.ROLE_NORMAL).get();

    student.getRoles().add(role);

    Student savedStudent = studentRepo.save(student);

    return modelMapper.map(savedStudent, RegisterStudentDTO.class);
  }

  @Override
  @Transactional
  public StudentDTO saveStudent(StudentDTO studentdto) {
    try {
      if (studentdto == null) {
        throw new APIException("studentDTO cannot be null");
      }

      Student student = modelMapper.map(studentdto, Student.class);
      if (student == null) {
        throw new APIException("Mapping studentDTO to Student failed");
      }

      Room room = roomRepo.findByRoomNumber(student.getRoom().getRoomNumber());
      if (room == null) {
        throw new APIException("Room not found");
      }

      student.setRoom(room);
      student.setProfileImagePath(null);

      Student savedStudent = studentRepo.save(student);

      return modelMapper.map(savedStudent, StudentDTO.class);
    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException
          && e.getCause().getMessage().contains("aadhar_card_number")) {
        throw new APIException("Aadhar Card already exists");
      } else {
        throw new APIException("Data integrity violation: " + e.getMessage());
      }
    } catch (Exception ex) {
      throw new APIException("An error occurred: " + ex.getMessage());
    }
  }

  @Override
  public List<StudentDTO> getAllStudents() {
    List<Student> studentsList = studentRepo.findAll();
    List<StudentDTO> listDTOs =
        studentsList.stream()
            .map((li) -> modelMapper.map(li, StudentDTO.class))
            .collect(Collectors.toList());
    return listDTOs;
  }

  @Override
  public StudentDTO getStudentById(int id) {
    Student student =
        studentRepo
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "Student ID ", id));
    StudentDTO studentdto = modelMapper.map(student, StudentDTO.class);

    if (studentdto.getProfileImagePath() != null && !studentdto.getProfileImagePath().isBlank()) {
      InputStream inputStream = null;
      ByteArrayOutputStream outputStream = null;
      try {
        inputStream = fileService.getResource(path, studentdto.getProfileImagePath());
        // Read image data from InputStream
        outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        studentdto.setProfileImageBytes(outputStream.toByteArray());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (inputStream != null) {
            inputStream.close();
          }
          if (outputStream != null) {
            outputStream.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return studentdto;
  }

  @Override
  public boolean isAadharCardNumberPresent(String aadharCardNumber) {
    return studentRepo.existsByAadharCardNumber(aadharCardNumber.trim());
  }

  @Override
  public Student getStudent(int id) {
    return studentRepo
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student", "Student ID", id));
  }

  @Override
  @Transactional
  public Student saveStudentFromPaymentServiceImpl(Student student) {
    return studentRepo.save(student);
  }

  @Override
  public List<StudentDTO> getStudentsByRoomId(Integer roomId) {
    List<Student> listOfStudents = studentRepo.findByRoomRoomNumber(roomId);
    List<StudentDTO> list =
        listOfStudents.stream()
            .map((li) -> modelMapper.map(li, StudentDTO.class))
            .collect(Collectors.toList());
    return list;
  }

  @Override
  @Transactional
  public void deleteStudentById(int id) {
    Student student = studentRepo.findById(id).orElseThrow();
    studentRepo.delete(student);
  }

  @Override
  public List<StudentDTO> getStudentsByFloorNumber(Integer floorNumber) {
    List<Student> listOfStudents = studentRepo.findByRoomFloorNumber(floorNumber);
    List<StudentDTO> listOfStudentsDTO =
        listOfStudents.stream()
            .map((li) -> modelMapper.map(li, StudentDTO.class))
            .collect(Collectors.toList());
    return listOfStudentsDTO;
  }

  @Override
  @Transactional
  public StudentDTO updateStudent(int studentId, StudentDTO studentdto) {
    Student student = studentRepo.findById(studentId).orElseThrow();

    student.setName(studentdto.getName());
    student.setEmail(studentdto.getEmail());
    student.setPhoneNumber(studentdto.getPhoneNumber());
    student.setAadharCardNumber(studentdto.getAadharCardNumber());
    student.setAddress(studentdto.getAddress());
    student.setRentday(studentdto.getRentday());

    if (student.getDueDate() == null) student.setDueDate(studentdto.getRentday());

    Room room = roomRepo.findByRoomNumber(student.getRoom().getRoomNumber());
    student.setRoom(room);

    student.setDateOfJoining(studentdto.getDateOfJoining());
    student.setProfileImagePath(studentdto.getProfileImagePath());
    student.setAadharCardImagePath(studentdto.getAadharCardImagePath());
    Student savedStudent = studentRepo.save(student);
    return modelMapper.map(savedStudent, StudentDTO.class);
  }

  @Override
  @Transactional
  public StudentDTO updateStudent2(
      int studentId,
      StudentDTO studentdto,
      MultipartFile profilePicture,
      MultipartFile aadharCardPicture) {
    Student student = studentRepo.findById(studentId).orElseThrow();
    student.setName(studentdto.getName());
    student.setEmail(studentdto.getEmail());
    student.setPhoneNumber(studentdto.getPhoneNumber());
    student.setAadharCardNumber(studentdto.getAadharCardNumber());
    student.setAddress(studentdto.getAddress());
    student.setRentday(studentdto.getRentday());

    if (student.getDueDate() == null) student.setDueDate(studentdto.getRentday());

    Room room = roomRepo.findByRoomNumber(studentdto.getRoom().getRoomNumber());
    student.setRoom(room);

    student.setDateOfJoining(studentdto.getDateOfJoining());

    try {
      String profilePicturefileName = null;
      String aadharCardPicturefileName = null;

      if (profilePicture != null) {
        profilePicturefileName = fileService.uploadImage(path, profilePicture);
        student.setProfileImagePath(profilePicturefileName);
      }
      if (aadharCardPicture != null) {
        aadharCardPicturefileName = fileService.uploadImage(path, aadharCardPicture);
        student.setAadharCardImagePath(aadharCardPicturefileName);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Student savedStudent = studentRepo.save(student);
    return modelMapper.map(savedStudent, StudentDTO.class);
  }

  @Override
  @Transactional
  public StudentDTO saveStudentWithImage(StudentDTOWithImage studentdtoWithImage) {
    StudentDTO studentdto = studentdtoWithImage.getStudentDTO();
    MultipartFile profileImage = studentdtoWithImage.getProfilePicture();
    MultipartFile aadharCardImage = studentdtoWithImage.getProfilePicture();

    Student student = modelMapper.map(studentdto, Student.class);
    Room room = roomRepo.findByRoomNumber(student.getRoom().getRoomNumber());

    student.setRoom(room);
    student.setDueDate(studentdto.getRentday());
    String profilePicturefileName = null;
    String aadharCardPicturefileName = null;

    if (profileImage != null) {
      try {
        profilePicturefileName = fileService.uploadImage(path, profileImage);
        aadharCardPicturefileName = fileService.uploadImage(path, aadharCardImage);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    student.setProfileImagePath(profilePicturefileName);
    student.setAadharCardImagePath(aadharCardPicturefileName);
    Student savedStudent = studentRepo.save(student);
    return modelMapper.map(savedStudent, StudentDTO.class);
  }

  @Override
  public List<StudentDTO> getStudentsPaymentStatus() {
    List<StudentDTO> data = this.getAllStudents();
    System.out.println("data : " + data);

    return data.stream().filter(StudentDTO::isPaymentDue).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void saveDefaultRolesOnStartup() {
    addRoleIfNotExists(501, "ROLE_ADMIN");
    addRoleIfNotExists(502, "ROLE_NORMAL");
  }

  private void addRoleIfNotExists(int id, String roleName) {
    if (!roleRepo.existsByName(roleName)) {
      Role userRole = new Role();
      userRole.setId(id);
      userRole.setName(roleName);
      roleRepo.save(userRole);
    }
  }
}
