package com.ragnar.hotel_reservation.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findRoomById(Long userId);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN :reservedRoomIds")
    List<Room> findAvailableRoomsExcept(@Param("reservedRoomIds") List<Long> reservedRoomIds);
}
