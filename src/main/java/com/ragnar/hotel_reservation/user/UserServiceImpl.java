package com.ragnar.hotel_reservation.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }
    @Override
    public User createUser(UserRegistrationRequest userRegistrationRequest) {
        User user = new User(
                userRegistrationRequest.email(),
                userRegistrationRequest.phoneNumber(),
                userRegistrationRequest.password(),
                userRegistrationRequest.firstname(),
                userRegistrationRequest.lastname()
        );
        return userRepository.save(user);
    }
}
