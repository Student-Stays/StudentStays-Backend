package com.management.studentstays.App.service;

import com.management.studentstays.App.payload.PaymentDTO;
import java.util.List;

public interface PaymentService {

  PaymentDTO createPayment(PaymentDTO paymentdto, int studentID);

  void updateIsDue(int studentId);

  List<PaymentDTO> getAllPayments(String aadharCard);
}
