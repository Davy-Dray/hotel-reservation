package com.ragnar.hotel_reservation.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @SequenceGenerator(
            sequenceName = "room_id_sequence",
            name = "room_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_id_sequence"
    )
    private Long id;


    private int roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    private double bookingPrice;

    public Room(int roomNumber, double bookingPrice) {
        this.roomNumber = roomNumber;
        this.bookingPrice = bookingPrice;
        this.roomStatus = RoomStatus.AVAILABLE;
    }
}
