package com.ragnar.hotel_reservation.room;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/rooms/")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<Room> getAllRooms(){
      return roomService.getAllRooms();
    }
    @GetMapping(path ="{roomId}")
    public Optional<Room> getRoomById(@PathVariable("roomId") Long roomId){
        return roomService.findRoomById(roomId);
    }
    @PostMapping
    public void addRoom(@RequestBody RoomRequest roomRequest){
        roomService.createRoom(roomRequest);
    }
    @DeleteMapping(path ="{roomId}")
    public void deleteRoomById(@PathVariable("roomId") Long roomId){
        roomService.deleteRoom(roomId);
    }
}
