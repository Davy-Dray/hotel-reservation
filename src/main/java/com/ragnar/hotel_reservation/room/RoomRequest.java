package com.ragnar.hotel_reservation.room;

public record RoomRequest(double bookingPrice,
                          String roomType,
                          String roomStatus,
                          int roomNumber
) {
}
