package com.ragnar.hotel_reservation.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import java.util.UUID;
import static com.ragnar.hotel_reservation.AbstractTestcontainers.FAKER;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findUserById() {

        // long id = 30;
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String phoneNumber = "08050569858";
        String password = "password";

        User user = new User(
                email,
                phoneNumber,
                password,
                firstName,
                lastName
        );
        // Save user
        underTest.save(user);


        long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();


        // Find user by ID
        Optional<User> actualUser = underTest.findUserById(id);

        assertThat(actualUser)
                .isPresent()
                .hasValueSatisfying(c
                        -> assertThat(c)
                        .isEqualToComparingFieldByField(user));

    }

    @Test
    void existsByEmail() {
        long id = 30;
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String phoneNumber = "08050569858";
        String password = "password";

        User user = new User(
                id,
                email,
                phoneNumber,
                password,
                firstName,
                lastName
        );
        // Save user
        underTest.save(user);

        // When
        var actual = underTest.existsByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsByPhoneNumber() {

        long id = 30;
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String phoneNumber = "08050569858";
        String password = "password";

        User user = new User(
                id,
                email,
                phoneNumber,
                password,
                firstName,
                lastName
        );
        // Save user
        underTest.save(user);

        // When
        var actual = underTest.existsByPhoneNumber(phoneNumber);

        // Then
        assertThat(actual).isTrue();
    }

}