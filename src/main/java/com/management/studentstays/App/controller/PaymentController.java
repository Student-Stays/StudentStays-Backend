package com.management.studentstays.App.controller;

import com.management.studentstays.App.payload.PaymentDTO;
import com.management.studentstays.App.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

  @Value("${razor_pay_id}")
  private String razorpayId;

  @Value("${razor_pay_secret}")
  private String razorpaySecretKey;

  @Autowired private PaymentService paymnetService;

  @PostMapping("/payment/createOrder")
  public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) {
    try {
      int amount = Integer.parseInt(data.get("amount").toString());
      Order order = getOrder(amount);
      return new ResponseEntity<>(order.toString(), HttpStatus.CREATED);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>("Invalid amount format", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error creating order", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public Order getOrder(int amount) {
    try {
      RazorpayClient razorpay = new RazorpayClient(razorpayId, razorpaySecretKey);

      JSONObject orderRequest = new JSONObject();
      orderRequest.put("amount", amount * 100);
      orderRequest.put("currency", "INR");
      orderRequest.put("receipt", "receipt#1");
      JSONObject notes = new JSONObject();
      notes.put("Checking", "Integrating razor pay");
      orderRequest.put("notes", notes);

      return razorpay.orders.create(orderRequest);
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Or throw a custom exception
    }
  }

  //	@PostMapping("/payment/{studentID}")
  //	@Transactional
  //	public ResponseEntity<PaymentDTO> pay(@RequestBody PaymentDTO paymentdto, @PathVariable String
  // studentID){
  //		System.out.println("----- pay -----");
  //		System.out.println("studentID : "+studentID);
  //		PaymentDTO paid = paymnetService.createPayment(paymentdto,Integer.parseInt(studentID));
  //		paymnetService.updateIsDue(Integer.parseInt(studentID));
  //		return new ResponseEntity<PaymentDTO>(paid, HttpStatus.OK);
  //	}

  @PostMapping("/payment/{studentID}")
  public ResponseEntity<PaymentDTO> pay(
      @RequestBody PaymentDTO paymentdto, @PathVariable String studentID) {
    PaymentDTO paid = paymnetService.createPayment(paymentdto, Integer.parseInt(studentID));
    return new ResponseEntity<PaymentDTO>(paid, HttpStatus.OK);
  }

  @PostMapping("/payment/isDue/{studentID}")
  public ResponseEntity<String> updateIsDueForStudent(@PathVariable String studentID) {
    paymnetService.updateIsDue(Integer.parseInt(studentID));
    return new ResponseEntity<String>("Successfull", HttpStatus.OK);
  }

  @GetMapping("/payments/{aadharCard}")
  public ResponseEntity<List<PaymentDTO>> getPaymentHistory(@PathVariable String aadharCard) {
    List<PaymentDTO> paid = paymnetService.getAllPayments(aadharCard);
    return new ResponseEntity<List<PaymentDTO>>(paid, HttpStatus.OK);
  }
}
