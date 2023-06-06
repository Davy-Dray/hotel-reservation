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
@Table(name = "AppUser",

        uniqueConstraints = {
        @UniqueConstraint(
                name = "user_id_unique",
                columnNames = "email"
        )
}

)
public class User {

    @Id
    @SequenceGenerator(
            name = "app_user_id_seq",
            sequenceName = "app_user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_id_seq"
    )
    private Long id;
    private String email;
    private String phoneNumber;
    private String password;
    private String firstname;
    private String lastname;

//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "reservation_id")
//    private Set<Reservation>userReservations;

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

    public User(Long id, String email, String phoneNumber, String password, String firstname, String lastname) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
