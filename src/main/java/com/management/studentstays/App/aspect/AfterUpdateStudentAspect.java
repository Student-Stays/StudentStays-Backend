package com.management.studentstays.App.aspect;

import com.management.studentstays.App.service.PaymentService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AfterUpdateStudentAspect {
  @Autowired private PaymentService paymentService;

  @Pointcut(
      "execution(* com.management.studentstays.App.impl.StudentServiceImpl.updateStudent2(..))")
  private void updateStudentMethod() {}

  @After("updateStudentMethod()")
  public void afterUpdateStudent(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Integer studentId = (Integer) args[0];
    paymentService.updateIsDue(studentId);
  }
}
