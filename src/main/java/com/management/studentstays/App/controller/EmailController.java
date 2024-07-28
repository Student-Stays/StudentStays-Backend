package com.management.studentstays.App.controller;

import com.management.studentstays.App.impl.EmailService;
import com.management.studentstays.App.payload.EmailDTO;
import com.management.studentstays.App.payload.EmailResponse;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {

  @Autowired private EmailService emailService;

  @PostMapping("/send-email/{templateName}")
  public ResponseEntity<EmailResponse> sendEmail(
      @RequestBody EmailDTO emailDTO, @PathVariable String templateName) {

    EmailResponse emailResponse = new EmailResponse();
    HashMap<String, String> emailContent = new HashMap<>();

    emailContent.put("SENDER_NAME", emailDTO.getName());
    emailContent.put("SENDER_EMAIL", emailDTO.getTo());
    emailContent.put("SENDER_SUBJECT", emailDTO.getSubject());
    emailContent.put("INQUIRY_MESSAGE", emailDTO.getText());

    emailContent.put("STUDENT_NAME", "TEST NAME");
    emailContent.put("PAYMENT_AMOUNT", "1700.00");
    emailContent.put("PAYMENT_MONTH", "MARCH");

    try {
      emailService.sendSimpleMessage(templateName, emailContent);
      emailResponse.setStatus(HttpStatus.OK);
      emailResponse.setMessage("Email sent successfully");
      return ResponseEntity.status(HttpStatus.OK).body(emailResponse);
    } catch (Exception e) {
      emailResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      emailResponse.setMessage("Failed to send email: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emailResponse);
    }
  }
}
