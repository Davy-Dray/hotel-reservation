package com.ragnar.hotel_reservation.reservation;

public record ReservationRequest(
        long userId,
        long roomId,

        String checkInDate,
        String checkOutDate
) {
}
