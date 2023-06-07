package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest extends AbstractTestcontainers {

    @Autowired
    ReservationRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findReservationById() {

    }

    @Test
    void findReservationsByUserId() {
    }

    @Test
    void findReservationsByTransactionId() {
    }

    @Test
    void existsByTransactionId() {
    }

    @Test
    void findReservationsByReservedRoom_RoomNumber() {
    }

    @Test
    void findOverlappingReservationsForRoom() {
    }

    @Test
    void findOverlappingReservations() {
    }

    @Test
    void findByCheckInDateTomorrow() {
    }

    @Test
    void findReservationsByCheckInDateAndStatus() {
    }

    @Test
    void findAllReservedRooms() {
    }
}