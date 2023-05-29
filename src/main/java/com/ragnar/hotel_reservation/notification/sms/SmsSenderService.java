package com.ragnar.hotel_reservation.notification.sms;

public interface SmsSenderService {
    void sendSms(SmsNotification smsNotification);
}

