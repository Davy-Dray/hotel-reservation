package com.ragnar.hotel_reservation.user;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserService {
    User findUserById(Long id);
    List<User> findAllUser();

    void createUser(UserRegistrationRequest userRegistrationRequest);

    void deleteUserById(Long id);

    void updateUser(Long id , UserUpdateRequest updateRequest);

    boolean existsByEmail(String email);

    boolean existByPhoneNumber(String phoneNumber);

    boolean existById(Long id);

}
