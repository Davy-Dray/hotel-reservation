package com.ragnar.hotel_reservation.room;

public record RoomUpdateRequest(String roomType,
                                String roomStatus,
                                int roomNumber,
                                double bookingPrice
) {
}
