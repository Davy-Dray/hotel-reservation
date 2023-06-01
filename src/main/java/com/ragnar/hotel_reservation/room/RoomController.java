package com.ragnar.hotel_reservation.room;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public Room getRoomById(@PathVariable("roomId") Long roomId){
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

    @PutMapping(path = "{roomId}")
    public void updateRoom(@PathVariable("roomId") Long roomId,
                           @RequestBody RoomUpdateRequest roomUpdateRequest){
        roomService.upDateRoom(roomUpdateRequest,roomId);
    }
    @GetMapping(path = "available" )
    public List<Room> findAvailableRooms(@RequestBody FindRoomRequest findRoomRequest){
      return roomService.findAvailableRooms(findRoomRequest);
    }
}
