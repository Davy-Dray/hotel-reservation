package com.ragnar.hotel_reservation.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserById(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

}
