package com.ragnar.hotel_reservation.notification.sms.template;

public record SmsReminderTemplate(
        String clientNumber,
        String transactionId
) {
}
