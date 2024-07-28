package com.management.studentstays.App.scheduler;

import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.service.PaymentService;
import com.management.studentstays.App.service.StudentService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentDueProcessor {

  @Autowired private StudentService service;

  @Autowired private ModelMapper modelMapper;

  @Autowired private PaymentService paymnetService;

  @Scheduled(cron = "0 11 0 * * ?")
  public void processPaymentDue() {
    System.out.println("----- SCHEDULER STARTING -----");

    List<StudentDTO> listOfstudent = service.getAllStudents();

    listOfstudent.forEach(
        (student) -> {
          System.out.println("student : " + student);
          paymnetService.updateIsDue(student.getId());
        });

    System.out.println("----- SCHEDULER CLOSING -----");
  }
}
