package com.management.studentstays.App.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(
    name = "rooms",
    indexes = {@Index(name = "room_number_index", columnList = "roomNumber")})
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "room_number", unique = true, nullable = false)
  private int roomNumber;

  @Column(name = "floor_number", nullable = false)
  private int floorNumber;

  @Column(name = "price", nullable = false)
  private double price;
}
