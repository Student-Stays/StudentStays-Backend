package com.management.studentstays.App.payload;

import com.management.studentstays.App.entity.Payment;
import com.management.studentstays.App.entity.Room;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class StudentResponseDTO {
  private int id;

  private String name;

  private String email;

  private String address;

  private String phoneNumber;

  private String aadharCardNumber;

  private String profileImagePath;

  private transient byte[] profileImageBytes;

  private String aadharCardImagePath;

  private Room room;

  private List<Payment> payments;

  private String dateOfJoining;

  private String rentday;

  private String dueDate;

  private boolean isPaymentDue;

  private String currentMonthPayment;

  private List<RoleDTO> roles = new ArrayList<>();
}
