package com.management.studentstays.App;

import com.management.studentstays.App.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class StudentStaysApplication implements CommandLineRunner {

  @Autowired private PasswordEncoder encoder;

  @Autowired private StudentService studentService;

  public static void main(String[] args) {
    SpringApplication.run(StudentStaysApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    String encodedPassword = encoder.encode("112233");
    System.out.println("Encoded password: " + encodedPassword);

    studentService.saveDefaultRolesOnStartup();
  }
}
