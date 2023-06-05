package com.ragnar.hotel_reservation.validation;

import com.ragnar.hotel_reservation.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputValidation {
    public static void validateReservationDates(LocalDate checkInDate, LocalDate checkOutDate) {

        LocalDate currentDate = LocalDate.now();
        if (checkInDate.isBefore(currentDate)) {
            throw new ValidationException("Check-in date must be equal to or greater than today's date.");
        }

        if (!checkInDate.isBefore(checkOutDate)) {
            throw new ValidationException("Check-in date must be before check-out date.");
        }
    }

    public static LocalDate parseLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        if (date != null) {
            return LocalDate.parse(date, formatter);
        }

        throw new ValidationException("Check-in date must be before check-out date.");
    }

}
