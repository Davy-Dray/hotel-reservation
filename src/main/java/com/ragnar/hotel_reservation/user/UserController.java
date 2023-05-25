package com.ragnar.hotel_reservation.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/users/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody UserRegistrationRequest registrationRequest){
        userService.createUser(registrationRequest);
    }

    @GetMapping
    public List<User> findAllUser(){
      return userService.findAllUser();
    }
    @GetMapping(path = {"{userId}"})
    public User findUserById(@PathVariable("userId") Long userId){
        return userService.findUserById(userId);
    }

}
