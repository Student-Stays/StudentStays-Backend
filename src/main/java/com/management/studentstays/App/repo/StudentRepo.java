package com.management.studentstays.App.repo;

import com.management.studentstays.App.entity.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Integer> {

  List<Student> findByRoomRoomNumber(Integer roomNumber);

  List<Student> findByRoomFloorNumber(Integer floorNumber);

  boolean existsByAadharCardNumber(String aadharCardNumber);

  Student findByAadharCardNumber(String aadharCardNumber);

  Student findByEmail(String email);
}
