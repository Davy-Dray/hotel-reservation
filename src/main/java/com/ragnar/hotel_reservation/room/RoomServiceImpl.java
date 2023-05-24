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
    public Optional<Room> findRoomById(Long id) {

        return Optional.ofNullable(
                roomRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        "customer [%s] not found".formatted(id)
                )));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public void deleteRoom(Long id) {
        if(!existsRoomWithId(id)){
            throw new ResourceNotFoundException(
                    "Room [%s] not found".formatted(id)
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
        try {
            return RoomType.valueOf(roomTypeName);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid room type");
        }
    }
    @Override
    public boolean existsRoomWithId(Long id) {
        return roomRepository.existsById(id);
    }


}
