package com.ragnar.hotel_reservation.notification.sms.template;

public record SmsCancellationTemplate(
        String clientName,
        String clientNumber,

        String transactionId
) {
}
