package com.ragnar.hotel_reservation.user;

import java.util.List;

public interface UserService {
    User findUserById(Long id);
    List<User> findAllUser();

    User createUser(UserRegistrationRequest userRegistrationRequest);

}
