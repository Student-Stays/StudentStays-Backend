package com.management.studentstays.App.exception;

import com.management.studentstays.App.payload.APIResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(
      ResourceNotFoundException ex) {
    String message = ex.getMessage();
    APIResponse response = new APIResponse(message, false);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(APIException.class)
  public ResponseEntity<APIResponse> handleAPIException(APIException ex) {
    String message = ex.getMessage();
    APIResponse response = new APIResponse(message, false);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponse> handleException(Exception ex) {
    ex.printStackTrace();
    String message = ex.getMessage();
    APIResponse response = new APIResponse(message, false);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, String> response = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String message = error.getDefaultMessage();
              response.put(fieldName, message);
            });

    return ResponseEntity.badRequest().body(response);
    //        return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DuplicateAadharCardException.class)
  public ResponseEntity<APIResponse> handleDuplicateAadharCardException(
      DuplicateAadharCardException ex) {
    APIResponse response = new APIResponse(ex.getMessage(), false);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(EmailTemplateNotFoundException.class)
  public ResponseEntity<APIResponse> handleEmailTemplateNotFoundException(
      EmailTemplateNotFoundException ex) {
    APIResponse response = new APIResponse(ex.getMessage(), false);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> exceptionHandler() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credentials Invalid.");
  }
}
