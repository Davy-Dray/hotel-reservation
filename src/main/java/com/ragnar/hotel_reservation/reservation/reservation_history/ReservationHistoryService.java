package com.ragnar.hotel_reservation.reservation.reservation_history;

import java.util.List;

public interface ReservationHistoryService {
    void createReservationHistory(ReservationHistory reservationHistory);
    List<ReservationHistory> getAllReservationHistory();

    ReservationHistory getReservationHistoryById(Long id);
}
