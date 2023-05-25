package com.ragnar.hotel_reservation.reservation;

import java.time.LocalDate;

public record ReservationRequest(
        long userId,
        long roomId,

        String checkInDate,
        String checkOutDate
) {
}
