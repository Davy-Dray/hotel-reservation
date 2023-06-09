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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "reservation_id_unique",
                        columnNames = "transactionId"
                )
        }
)
public class Reservation {

    @Id
    @SequenceGenerator(
            sequenceName = "reservation_id_seq",
            name = "reservation_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_id_seq"
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.MERGE)
    private Room reservedRoom;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
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
        this.user = user;
        this.reservedRoom = reservedRoom;
        this.totalCharge = getTotalFee();
        this.transactionId = transactionId;
        this.status = ReservationStatus.PENDING;
    }

    double getTotalFee(){
        long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return reservedRoom.getBookingPrice()*daysBetween;
    }
}
