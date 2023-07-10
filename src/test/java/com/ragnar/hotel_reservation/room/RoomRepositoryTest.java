package com.ragnar.hotel_reservation.room;

import com.ragnar.hotel_reservation.AbstractTestcontainers;
import com.ragnar.hotel_reservation.user.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest extends AbstractTestcontainers {


    @Autowired
    RoomRepository underTest;
    @Test
    void findRoomById() {


        Room room = new Room(
                1L,
                104,
                RoomType.DELUXE,
                RoomStatus.AVAILABLE,
                20000
        );

        underTest.save(room);


        Optional<Room> actualRoom = underTest.findRoomById(1L);
        assertThat(actualRoom)
                .isPresent()
                .hasValueSatisfying(c
                        -> assertThat(c)
                        .isEqualToComparingFieldByField(room));
    }

//    @Test
//    void findAvailableRoomsExcept() {
//    }
}