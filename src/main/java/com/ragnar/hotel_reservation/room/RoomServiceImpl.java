package com.ragnar.hotel_reservation.room;

import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public Room findRoomById(Long id) {
        Optional<Room> room = roomRepository.findRoomById(id);
        if(room.isEmpty()){
            throw new ResourceNotFoundException(
                    "Room with id [%s] not found".formatted(id)
            );
        }
      return room.get();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public void deleteRoom(Long id) {
        if(existsRoomWithId(id)){
            throw new ResourceNotFoundException(
                    "Room with id [%s] not found".formatted(id)
            );
        }
        roomRepository.deleteById(id);
    }

    @Override
    public void createRoom(RoomRequest roomRequest) {
        Room room = new Room(roomRequest.roomNumber(), roomRequest.bookingPrice());
        RoomType roomType = parseRoomType(roomRequest.roomType());
        room.setRoomType(roomType);
        roomRepository.save(room);
    }

    private RoomType parseRoomType(String roomTypeName) {
            return RoomType.valueOf(roomTypeName.toUpperCase());
    }
    private RoomStatus parseRoomStatus(String roomStatusName) {
        

            return RoomStatus.valueOf(roomStatusName.toUpperCase());
    }
    @Override
    public boolean existsRoomWithId(Long id) {
        return !roomRepository.existsById(id);
    }

    @Override
    public void upDateRoom(RoomUpdateRequest request,Long roomId){
        if(existsRoomWithId(roomId)){
            throw new ResourceNotFoundException(
                    "Room with id [%s] not found".formatted(roomId)
            );
        }
        Room room = findRoomById(roomId);
        RoomType roomType = parseRoomType(request.roomType());
        RoomStatus roomStatus = parseRoomStatus(request.roomStatus());
        room.setRoomType(roomType);
        room.setRoomStatus(roomStatus);
        room.setRoomNumber(request.roomNumber());
        roomRepository.save(room);

    }

}
