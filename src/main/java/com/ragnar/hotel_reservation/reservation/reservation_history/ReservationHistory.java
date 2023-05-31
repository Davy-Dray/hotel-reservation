package com.ragnar.hotel_reservation.reservation.reservation_history;


import com.ragnar.hotel_reservation.reservation.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationHistory {
    @Id
    @SequenceGenerator(
            sequenceName = "reservationHistory_id_sequence",
            name = "reservationHistory_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservationHistory_id_sequence"
    )
    private Long id;

    @OneToOne
    Reservation reservation;

    String actionPerformed;

    LocalDateTime dateTime;

    public ReservationHistory(Reservation reservation, String actionPerformed) {
        this.reservation = reservation;
        this.actionPerformed = actionPerformed;
        this.dateTime = LocalDateTime.now();
    }
}
