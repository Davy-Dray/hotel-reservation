package com.ragnar.hotel_reservation.room;

import java.util.List;

public interface RoomService {

    Room findRoomById(Long id);

    List<Room> getAllRooms();

    void deleteRoom(Long id);

    void createRoom(RoomRequest roomRequest);

    boolean existsRoomWithId(Long id);

    void upDateRoom(RoomUpdateRequest request,Long roomId);

    List<Room>findAvailableRooms(FindRoomRequest findRoomRequest);
}
