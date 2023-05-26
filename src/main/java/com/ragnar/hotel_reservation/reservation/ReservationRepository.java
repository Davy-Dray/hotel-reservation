package com.ragnar.hotel_reservation.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation>findReservationById(Long reservationId);
    List<Reservation> findReservationsByUserId(Long userId);

    Optional<Reservation> findReservationsByTransactionId(String transactionId);
    boolean existsByTransactionId(String transactionId);

    Optional<Reservation>findReservationsByReservedRoom_RoomNumber(int id);

    @Query("SELECT r FROM Reservation r WHERE r.checkOutDate > :checkInDate AND r.checkInDate < :checkOutDate")
    List<Reservation> findOverlappingReservations(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

}
