package com.management.studentstays.App.payload;

import lombok.Data;

@Data
public class PaymentDTO {

  private int id;

  private String razorpayPaymentId;

  private String paymentDate;

  private double amount;

  private boolean paymentStatus;

  private String month;

  private int year;
}
