package com.ragnar.hotel_reservation.notification.sms;

public record SmsNotification(
        String receiverPhoneNumber,
        String message
) {

}
