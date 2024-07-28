package com.management.studentstays.App.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments") // Explicitly specify the table name
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String razorpayPaymentId;

  private String paymentDate;

  private double amount;

  private boolean paymentStatus; // true if payment is done, false otherwise

  private String month;

  private String year;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "aadhar_card_number", referencedColumnName = "aadhar_card_number")
  @JsonBackReference
  private Student student;
}
