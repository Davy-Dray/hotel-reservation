package com.ragnar.hotel_reservation.notification.sms.template;

import java.time.LocalDate;

public record SmsConfirmationTemplate(
        String clientName,
        String clientNumber,
        LocalDate checkInDate,
        String roomType,
        String transactionId
) {
}
