package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.AbstractTestcontainers;
import com.ragnar.hotel_reservation.room.Room;
import com.ragnar.hotel_reservation.room.RoomRepository;
import com.ragnar.hotel_reservation.room.RoomStatus;
import com.ragnar.hotel_reservation.room.RoomType;
import com.ragnar.hotel_reservation.user.User;
import com.ragnar.hotel_reservation.user.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest extends AbstractTestcontainers {

//    @Autowired
//    ReservationRepository underTest;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    void findOverlappingReservationsForRoom() {
//        Long roomId = 1L;
//        LocalDate checkInDate = LocalDate.of(2023, 6, 1);
//        LocalDate checkOutDate = LocalDate.of(2023, 6, 5);
//
//        List<Reservation> overlappingReservations =
//                underTest.findOverlappingReservationsForRoom(
//                        roomId,
//                        checkInDate,
//                        checkOutDate
//                );
//        assertNotNull(overlappingReservations);
//        assertEquals(0, overlappingReservations.size());
//    }
//
//
//    @Test
//    void findOverlappingReservations() {
//    }
//
//    @Test
//    void findByCheckInDateTomorrow() {
//    }
//
//    @Test
//    void findReservationsByCheckInDateAndStatus() {
//    }
//
//    @Test
//    void findAllReservedRooms() {
//    }
}