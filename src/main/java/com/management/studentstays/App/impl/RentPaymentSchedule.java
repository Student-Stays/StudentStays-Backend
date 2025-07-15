package com.management.studentstays.App.impl;

import com.management.studentstays.App.entity.Payment;
import com.management.studentstays.App.entity.Student;
import com.management.studentstays.App.payload.StudentDTO;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentPaymentSchedule {

  @Autowired private ModelMapper modelMapper;

  public boolean isPaymentDue(StudentDTO student) {
    try {


      // handle the case where student.getDueDate is null
      if (student.getDueDate() == null) return false;
      String rentDayString = student.getDueDate().trim();
      LocalDate rentDay = LocalDate.parse(rentDayString);
      LocalDate currentDate = LocalDate.now();
      //            LocalDate currentDate = LocalDate.parse("2024-10-10");

      return (currentDate.isEqual(rentDay) || currentDate.isAfter(rentDay))
          && !isPaymentMade(student);



    } catch (DateTimeParseException e) {
      System.err.println("Error parsing rent day: " + e.getMessage());
      return false;
    } catch (Exception e) {
      System.err.println("Error checking payment due: " + e.getMessage());
      return false;
    }
  }

  public boolean isPaymentMade(StudentDTO student) {
    List<Payment> paymentHistory = student.getPayments();
    if (paymentHistory.isEmpty()) return false;

    LocalDate date = LocalDate.parse(student.getDueDate());
    String monthName = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    String year = String.valueOf(date.getYear());
    for (Payment payment : paymentHistory) {
      if (payment.getMonth().equals(monthName)
          && payment.getYear().equals(year)
          && payment.isPaymentStatus()) {
        return true;
      }
    }
    return false;
  }

  public Student updateDueDate(Student student) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date rentDay = sdf.parse(student.getDueDate());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(rentDay);
      calendar.add(Calendar.MONTH, 1);
      student.setDueDate(sdf.format(calendar.getTime()));
      return student;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
