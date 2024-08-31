package com.management.studentstays.App.security;

import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.repo.StudentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

  @Autowired private StudentRepo studentRepo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Load user from database by username
    log.info("-------- Custom User Details Service --------");
    Student student = this.studentRepo.findByEmail(email);
    return org.springframework.security.core.userdetails.User.builder()
        .username(student.getEmail())
        .password(student.getPassword())
        // Add roles if applicable
        // .authorities(student.getRoles())
        .build();
  }
}
