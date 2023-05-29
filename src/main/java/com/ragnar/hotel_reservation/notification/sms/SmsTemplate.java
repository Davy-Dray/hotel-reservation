package com.ragnar.hotel_reservation.notification.sms;

import java.time.LocalDate;

public record SmsTemplate(
        String clientName,
        String clientNumber,
        LocalDate checkInDate,
        String roomType,
        String transactionId
) {
}
