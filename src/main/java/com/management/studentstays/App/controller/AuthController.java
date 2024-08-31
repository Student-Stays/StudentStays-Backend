package com.management.studentstays.App.controller;

import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.payload.JWTRequest;
import com.management.studentstays.App.payload.JWTResponse;
import com.management.studentstays.App.payload.RegisterStudentDTO;
import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.repo.StudentRepo;
import com.management.studentstays.App.security.JWTTokenHelper;
import com.management.studentstays.App.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private AuthenticationManager manager;

  @Autowired private StudentRepo studentRepo;

  @Autowired private ModelMapper modelMapper;

  @Autowired private JWTTokenHelper helper;

  @Autowired private StudentService studentService;

  @PostMapping("/login")
  public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
    this.doAuthenticate(request.getEmail(), request.getPassword());
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    String token = this.helper.generateToken(userDetails);

    Student student = studentRepo.findByEmail(request.getEmail());
    StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);

    JWTResponse response =
        JWTResponse.builder()
            .jwtToken(token)
            .userName(userDetails.getUsername())
            .studentId(String.valueOf(studentDTO.getId()))
            .role(studentDTO.getRoles().get(0).getName())
            .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private void doAuthenticate(String email, String password) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(email, password);
    try {
      manager.authenticate(authentication);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException(" Invalid Username or Password  !!");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterStudentDTO> registerNewUser(
      @RequestBody RegisterStudentDTO studentDTO) {
    RegisterStudentDTO registeredUser = studentService.registerNewUser(studentDTO);
    return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
  }
}
