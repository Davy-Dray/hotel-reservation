package com.ragnar.hotel_reservation.reservation;

import java.time.LocalDate;

public record ReservationRequest(
        long userId,
        long roomId,

        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
