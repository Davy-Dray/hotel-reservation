package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.room.Room;
import com.ragnar.hotel_reservation.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @SequenceGenerator(
            sequenceName = "reservation_id_sequence",
            name = "reservation_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_id_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column(nullable = false)
    private boolean hasCheckedIn;
    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Room reservedRoom;

    private double totalCharge;
    private String transactionId;


    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("d/M/yyyy");

    public Reservation(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            User user,
            String transactionId,
            Room reservedRoom
    ) {
        this.checkInDate = LocalDate.parse(
                DATE_TIME_FORMATTER.format(checkInDate),
                DATE_TIME_FORMATTER
        );
        this.checkOutDate = LocalDate.parse(
                DATE_TIME_FORMATTER.format(checkOutDate),
                DATE_TIME_FORMATTER
        );
        this.createdAt = LocalDate.parse(
                DATE_TIME_FORMATTER.format(LocalDateTime.now()),
                DATE_TIME_FORMATTER
        );

        this.hasCheckedIn = false;
        this.user = user;
        this.reservedRoom = reservedRoom;
        this.totalCharge = getTotalFee();
        this.transactionId = transactionId;
    }

    double getTotalFee(){
        long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return reservedRoom.getBookingPrice()*daysBetween;
    }
}
