package com.ragnar.hotel_reservation.user;

import com.ragnar.hotel_reservation.exception.DuplicateResourceException;
import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static com.ragnar.hotel_reservation.AbstractTestcontainers.FAKER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository);
    }
    @Test
    void findUserById() {

        long id = 10;
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
        // When
        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        //Then
        User actual = underTest.findUserById(10L);
        assertThat(actual).isEqualTo(user);
    }


    @Test
    void willThrowWhenGetUserReturnsEmptyOptional() {
        //GIVEN
        long id = 10;
        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        //THEN
        assertThatThrownBy(() -> underTest.findUserById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "user with id [%s] not found".formatted(id)
                );
    }
    @Test
    void createUser() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String phoneNumber = "08050569858";
        String password = "password";

        UserRegistrationRequest request = new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                password,
                phoneNumber
        );
        // When
        underTest.createUser(request);

        // Verify that the save method was called with the expected User object
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();


        assertEquals(request.email(), capturedUser.getEmail());
        assertEquals(request.phoneNumber(), capturedUser.getPhoneNumber());
        assertEquals(request.password(), capturedUser.getPassword());
        assertEquals(request.firstname(), capturedUser.getFirstname());
        assertEquals(request.lastname(), capturedUser.getLastname());
    }


    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "alex@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);


        UserRegistrationRequest request = new UserRegistrationRequest(
                "david",
                "egbedina",
                email,
                "password",
                "08050569858"
        );
        // When
        assertThatThrownBy(() -> underTest.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(userRepository, never()).save(any());
    }


    @Test
    void findAllUser() {
        //WHEN
        underTest.findAllUser();
        //THEN
        verify(userRepository).findAll();
    }


    @Test
    void deleteUserById() {
        long id = 10;

        when(userRepository.existsById(id)).thenReturn(true);

        // When
        underTest.deleteUserById(id);
        // Then
        verify(userRepository).deleteById(id);
    }


    @Test
    void willThrowUserByIdNotExists() {
        // Given
        long id = 10;

        when(userRepository.existsById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest
                .deleteUserById(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "user with id [%s] not found".formatted(id)
                );

        // Then
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void existsByEmail() {
        String email = "dave@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        underTest.existsByEmail(email);
        // Then
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void existByPhoneNumber() {
        String phoneNumber = "08050569858";

        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        // When
        underTest.existByPhoneNumber(phoneNumber);
        // Then
        verify(userRepository).existsByPhoneNumber(phoneNumber);
    }

    @Test
    void existById() {
        long id = 10;

        when(userRepository.existsById(id)).thenReturn(true);

        // When
        underTest.existById(id);
        // Then
        verify(userRepository).existsById(id);
    }


        @Test
        void updateUser_SuccessfullyUpdatesUser() {

            Long userId = 1L;

            User existingUser = new User(
                    userId,
                    "test@example.com",
                    "08050569858",
                    "password",
                    "John",
                    "Doe"
            );

            UserUpdateRequest updateRequest = new UserUpdateRequest(
                    "jermain",
                    "cole",
                    "cole@gmail.com",
                    "User",
                    "081541866643"
            );

            when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

            when(userRepository.existsByEmail(updateRequest.email())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(updateRequest.phoneNumber())).thenReturn(false);

            underTest.updateUser(userId, updateRequest);

            verify(userRepository).findUserById(userId);

            verify(userRepository).existsByEmail(updateRequest.email());
            verify(userRepository).existsByPhoneNumber(updateRequest.phoneNumber());

            verify(userRepository).save(existingUser);

            assertEquals(updateRequest.email(), existingUser.getEmail());
            assertEquals(updateRequest.phoneNumber(), existingUser.getPhoneNumber());
            assertEquals(updateRequest.firstname(), existingUser.getFirstname());
            assertEquals(updateRequest.lastname(), existingUser.getLastname());
        }

        @Test
        void updateUser_ThrowsExceptionWhenEmailAlreadyTaken() {

            Long userId = 1L;

            User existingUser = new User(
                    userId,
                    "test@example.com",
                    "08050569858",
                    "password",
                    "John",
                    "Doe"
            );

            // Create a UserUpdateRequest with test data
            UserUpdateRequest updateRequest = new UserUpdateRequest(
                    "jermain",
                    "cole",
                    "cole@gmail.com",
                    "User",
                    "081541866643"
            );
            when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

            when(userRepository.existsByEmail(updateRequest.email())).thenReturn(true);

            assertThrows(DuplicateResourceException.class, () -> underTest.updateUser(userId, updateRequest));

            verify(userRepository).findUserById(userId);

            verify(userRepository).existsByEmail(updateRequest.email());

            verify(userRepository, never()).save(existingUser);
        }

        @Test
        void updateUser_ThrowsExceptionWhenPhoneNumberAlreadyTaken() {

            // Create a test user ID
            Long userId = 1L;

            // Create a test User object
            User existingUser = new User(
                    userId,
                    "test@example.com",
                    "08050569858",
                    "password",
                    "John",
                    "Doe"
            );

            // Create a UserUpdateRequest with test data
            UserUpdateRequest updateRequest = new UserUpdateRequest(
                    "jermain",
                    "cole",
                    "cole@gmail.com",
                    "User",
                    "081541866643"
            );
            when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

            when(userRepository.existsByPhoneNumber(updateRequest.phoneNumber())).thenReturn(true);

            assertThrows(DuplicateResourceException.class, () -> underTest.updateUser(userId, updateRequest));

            verify(userRepository).findUserById(userId);

            verify(userRepository).existsByPhoneNumber(updateRequest.phoneNumber());

            verify(userRepository, never()).save(existingUser);
        }
    }


