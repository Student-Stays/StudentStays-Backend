package com.management.studentstays.App.impl;

import com.management.studentstays.App.entity.Payment;
import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.payload.PaymentDTO;
import com.management.studentstays.App.payload.StudentDTO;
import com.management.studentstays.App.repo.PaymentRepo;
import com.management.studentstays.App.service.PaymentService;
import com.management.studentstays.App.service.StudentService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

  @Autowired private ModelMapper modelMapper;

  @Autowired private PaymentRepo paymentrepo;

  @Autowired private StudentService studentService;

  @Autowired private RentPaymentSchedule rentPaymentSchedule;

  @Autowired private EmailService emailService;

  @Override
  public PaymentDTO createPayment(PaymentDTO paymentdto, int studentID) {
    Student student = studentService.getStudent(studentID);
    Payment payment = modelMapper.map(paymentdto, Payment.class);

    payment.setStudent(student);
    payment.setPaymentStatus(true);

    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
    String paymentDate = today.format(formatter);
    payment.setPaymentDate(paymentDate);

    Payment savedPayment = paymentrepo.save(payment);
    sendEmailToOwner(student, savedPayment);
    return modelMapper.map(savedPayment, PaymentDTO.class);
  }

  public void sendEmailToOwner(Student student, Payment savedPayment) {
    HashMap<String, String> emailContent = new HashMap<>();
    emailContent.put("studentName", student.getName());
    String amountFormatted = String.format("%.2f", savedPayment.getAmount());
    emailContent.put("amount", amountFormatted);

    // issue in student.getPayments()
    //    emailContent.put(
    //        "month", student.getPayments().get(student.getPayments().size() - 1).getMonth());

    emailService.sendSimpleMessage("paymentReceived", emailContent);
  }

  public void updateIsDue(int studentID) {
    Student student = studentService.getStudent(studentID);
    if (student == null) {
      System.out.println("Student not found with ID: " + studentID);
      return;
    }

    try {
      StudentDTO studentdto = modelMapper.map(student, StudentDTO.class);

      boolean isDue = rentPaymentSchedule.isPaymentDue(studentdto);
      boolean isPaid = rentPaymentSchedule.isPaymentMade(studentdto);

      if (isDue && !isPaid) {
        // Payment is due and not paid
        student.setPaymentDue(true);
      } else if (isPaid) {
        student.setPaymentDue(false);
        Student updatedStudent =
            rentPaymentSchedule.updateDueDate(student); // Update due date for next month
        studentService.saveStudentFromPaymentServiceImpl(updatedStudent);
        return;
      } else {
        student.setPaymentDue(false);
      }
      Student student1 = studentService.saveStudentFromPaymentServiceImpl(student);
    } catch (Exception e) {
      System.out.println("Error occurred while mapping student information: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public List<PaymentDTO> getAllPayments(String aadharCard) {
    List<Payment> list = paymentrepo.findByAadharCard(aadharCard);
    List<PaymentDTO> listOfDTOs =
        list.stream()
            .map((li) -> modelMapper.map(li, PaymentDTO.class))
            .collect(Collectors.toList());
    return listOfDTOs;
  }
}
