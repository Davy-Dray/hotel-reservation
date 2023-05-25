package com.ragnar.hotel_reservation.user;

public record UserUpdateRequest(String firstname,
                                String lastname,
                                String email,
                                String password,
                                String phoneNumber
) {}