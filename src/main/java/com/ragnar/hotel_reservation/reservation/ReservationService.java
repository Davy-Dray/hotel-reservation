package com.ragnar.hotel_reservation.reservation;

import java.util.List;

public interface ReservationService {

    Reservation findReservationById(Long id);

    List <Reservation> findAllReservations();

    void createReservation(ReservationRequest reservationRequest);

    void checkClientInOrOut(String reservationId);

    boolean existsReservationById(Long id);

    List<Reservation> getAllReservationsForAUser(Long id);

    void deleteReservation(Long id);

    Reservation findReservationByTransactionId(String transactionId);

    Reservation findReservationByRoomNumber(int id);

    void cancelReservation(String transactionId);
}
