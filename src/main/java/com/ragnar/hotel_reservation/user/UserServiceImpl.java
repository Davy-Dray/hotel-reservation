package com.ragnar.hotel_reservation.user;

import com.ragnar.hotel_reservation.exception.DuplicateResourceException;
import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findUserById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException(
                    "user with id [%s] not found".formatted(id)
            );
        }
        return user.get();
    }
    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }
    @Override
    public void createUser(UserRegistrationRequest userRegistrationRequest) {
        User user = new User(
                userRegistrationRequest.email(),
                userRegistrationRequest.phoneNumber(),
                userRegistrationRequest.password(),
                userRegistrationRequest.firstname(),
                userRegistrationRequest.lastname()
        );
         userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
       if (!existById(id)){
           throw new ResourceNotFoundException(
                   "user with id [%s] not found".formatted(id)
           );
       }
      userRepository.deleteById(id);
    }

    @Override
    public void updateUser(Long id, UserUpdateRequest updateRequest) {
        User user = findUserById(id);
        if (existsByEmail(updateRequest.email())){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        if (existByPhoneNumber(updateRequest.phoneNumber())){
            throw new DuplicateResourceException(
                    "phone number already taken"
            );
        }
        user.setFirstname(updateRequest.firstname());
        user.setLastname(updateRequest.lastname());
        user.setEmail(updateRequest.email());
        user.setPhoneNumber(updateRequest.phoneNumber());
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }


}
