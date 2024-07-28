package com.management.studentstays.App.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role {

  @Id private int id;

  @Column(name = "role_name")
  private String name;
}
