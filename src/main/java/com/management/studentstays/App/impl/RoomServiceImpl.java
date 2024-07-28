package com.management.studentstays.App.impl;

import com.management.studentstays.App.entity.Room;
import com.management.studentstays.App.payload.RoomDTO;
import com.management.studentstays.App.repo.RoomRepo;
import com.management.studentstays.App.service.RoomService;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

  @Autowired private RoomRepo roomRepo;

  @Autowired private ModelMapper modelMapper;

  @Override
  public RoomDTO saveRoom(RoomDTO room) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Integer> getAllRooms() {
    List<Room> list = roomRepo.findAll();

    List<Integer> roomNumbers = new ArrayList<>();
    for (Room room : list) {
      roomNumbers.add(room.getRoomNumber());
    }

    return roomNumbers;
  }

  @Override
  public RoomDTO getRoomById(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteRoomById(int id) {
    // TODO Auto-generated method stub

  }
}
