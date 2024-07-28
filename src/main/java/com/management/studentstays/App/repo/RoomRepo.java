package com.management.studentstays.App.repo;

import com.management.studentstays.App.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room, Integer> {
  Room findByRoomNumber(int roomNumber);
}
