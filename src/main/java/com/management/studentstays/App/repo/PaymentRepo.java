package com.management.studentstays.App.repo;

import com.management.studentstays.App.entity.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

  @Query("SELECT p FROM Payment p WHERE p.student.aadharCardNumber = :aadharCard")
  List<Payment> findByAadharCard(@Param("aadharCard") String aadharCard);
}
