package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.AbstractTestcontainers;
import com.ragnar.hotel_reservation.room.Room;
import com.ragnar.hotel_reservation.room.RoomRepository;
import com.ragnar.hotel_reservation.room.RoomStatus;
import com.ragnar.hotel_reservation.room.RoomType;
import com.ragnar.hotel_reservation.user.User;
import com.ragnar.hotel_reservation.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest extends AbstractTestcontainers {

    @Autowired
    ReservationRepository underTest;

    @Mock
    UserRepository userRepository;

    @Mock
    RoomRepository roomRepository;
    @BeforeEach
    void setUp() {
    }

    @Test
    void findReservationById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String phoneNumber = "08050569858";
        String password = "password";

        User user = new User(
                1L,
                email,
                phoneNumber,
                password,
                firstName,
                lastName
        );
        userRepository.save(user);



        long id = userRepository.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();

        User t = userRepository.findUserById(id).orElseThrow();

        Room room = new Room(
                11L,
                104,
                RoomType.DELUXE,
                RoomStatus.AVAILABLE,
                20000
        );

        LocalDate checkInDate = LocalDate.of(2023, 6, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 6, 5);

        String transactionId = "welcome123";
        Reservation reservation = new Reservation(
                checkInDate,
                checkOutDate,
                t,
                transactionId,
                room
        );
        underTest.save(reservation);

        long rid = underTest.findAll()
                .stream()
                .filter(c -> c.getTransactionId().equals(transactionId))
                .map(Reservation::getId)
                .findFirst()
                .orElseThrow();

        Optional<Reservation> existingReservation = underTest.findReservationById(rid);

        assertThat(existingReservation)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c).isEqualToComparingFieldByField(reservation));
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
        Long roomId = 1L;
        LocalDate checkInDate = LocalDate.of(2023, 6, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 6, 5);

        List<Reservation> overlappingReservations =
                underTest.findOverlappingReservationsForRoom(
                        roomId,
                        checkInDate,
                        checkOutDate
                );
        assertNotNull(overlappingReservations);
        assertEquals(0, overlappingReservations.size());
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