package com.management.studentstays.App.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class EmailTemplate {
  @Id private Long id;

  private String name;
  private String recipient;
  private String subject;
}
