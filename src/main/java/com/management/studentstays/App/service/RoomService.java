package com.management.studentstays.App.service;

import com.management.studentstays.App.payload.RoomDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {

  RoomDTO saveRoom(RoomDTO room);

  List<Integer> getAllRooms();

  RoomDTO getRoomById(int id);

  void deleteRoomById(int id);
}
