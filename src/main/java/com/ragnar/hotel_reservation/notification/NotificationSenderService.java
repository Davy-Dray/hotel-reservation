package com.ragnar.hotel_reservation.notification;


import com.ragnar.hotel_reservation.notification.sms.SmsNotification;
import com.ragnar.hotel_reservation.notification.sms.SmsTemplate;
import com.ragnar.hotel_reservation.notification.sms.twilio.TwilioSmsSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class NotificationSenderService {

  private final  TwilioSmsSenderService twilioSmsSenderService;


    public void sendBookingConfirmationSms(SmsTemplate template){

        String message = "Dear " + template.clientName() +
                ", your booking has been confirmed for " + template.checkInDate() +
                ". Your booking ID is " + template.transactionId() +
                ", room " + template.roomType() + ".";

        twilioSmsSenderService.sendSms(new SmsNotification(
                template.clientNumber(),
                message
        ));

    }

}



//    String roomType,
//    String transactionId