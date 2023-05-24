package com.ragnar.hotel_reservation.reservation;

import java.util.List;

public interface ReservationService {

    Reservation findReservationById(Long id);

    List <Reservation> findAllReservations();

    Reservation createReservation(ReservationRequest reservationRequest);

    void updateReservationStatus(Long id);
}
