package com.ragnar.hotel_reservation.reservation;

public record ReservationUpdateRequest(
        long roomId,
        String checkInDate,
        String checkOutDate
) {
}
