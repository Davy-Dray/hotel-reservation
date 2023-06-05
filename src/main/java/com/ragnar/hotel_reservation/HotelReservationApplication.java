package com.ragnar.hotel_reservation;

import com.ragnar.hotel_reservation.user.UserRegistrationRequest;
import com.ragnar.hotel_reservation.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelReservationApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HotelReservationApplication.class, args);
    }

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) {
        userService.createUser(
                new UserRegistrationRequest(
                "David",
                "Egbedina",
                "david.egbedina@gmail.com",
                "dray@gmail.com",
                "08050569858"
        ));
    }
}
