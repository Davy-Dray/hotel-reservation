package com.ragnar.hotel_reservation.user;

public record UserRegistrationRequest(String firstname,
                                      String lastname,
                                      String email,
                                      String password,
                                      String phoneNumber
                                      ) {
}
