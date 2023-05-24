package com.ragnar.hotel_reservation.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ragnar.hotel_reservation.reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "AppUser")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Long id;
    private String email;
    private String phoneNumber;
    private String password;
    private String firstname;
    private String lastname;

    @OneToMany
    @JsonIgnore
    private Set<Reservation>userReservations;

    public User(String email,
                String phoneNumber,
                String password,
                String firstname,
                String lastname
    ) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
