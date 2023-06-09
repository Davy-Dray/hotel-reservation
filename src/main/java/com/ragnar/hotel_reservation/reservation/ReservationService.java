package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.room.Room;

import java.util.List;

public interface ReservationService {

    Reservation findReservationById(Long id);

    List <Reservation> findAllReservations();

    void createReservation(ReservationRequest reservationRequest);

    void checkClientIn(String reservationId);

    void checkClientOut(String reservationId);


    boolean existsReservationById(Long id);

    List<Reservation> getAllReservationsForAUser(Long id);

    void deleteReservation(Long id);

    Reservation findReservationByTransactionId(String transactionId);

    Reservation findReservationByRoomNumber(int id);

    List<Room>findAllReservedRooms();

    void cancelReservation(String transactionId);

    void updateReservation(Long id, ReservationUpdateRequest updateRequest);
}
