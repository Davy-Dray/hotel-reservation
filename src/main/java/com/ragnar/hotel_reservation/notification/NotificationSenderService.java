package com.ragnar.hotel_reservation.notification;


import com.ragnar.hotel_reservation.notification.sms.template.SmsCancellationTemplate;
import com.ragnar.hotel_reservation.notification.sms.template.SmsConfirmationTemplate;
import com.ragnar.hotel_reservation.notification.sms.SmsNotification;
import com.ragnar.hotel_reservation.notification.sms.template.SmsReminderTemplate;
import com.ragnar.hotel_reservation.notification.sms.twilio.TwilioSmsSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationSenderService {

  private final  TwilioSmsSenderService twilioSmsSenderService;


    public void sendBookingConfirmationSms(SmsConfirmationTemplate template){
        String message = "Dear " + template.clientName() +
                ", your booking has been confirmed for " + template.checkInDate() +
                ". Your booking ID is " + template.transactionId() +
                ", room " + template.roomType() + ".";

        twilioSmsSenderService.sendSms(new SmsNotification(
                template.clientNumber(),
                message
        ));

    }
    public void sendBookingCancellationSms(SmsCancellationTemplate template){
        String message ="Dear"+template.clientName()+","+"\n"+
                         "your booking "+template.transactionId()+" " +
                         "has been cancelled.";

        twilioSmsSenderService.sendSms(
                new SmsNotification(
                        template.clientNumber(),
                        message
                )
        );
    }

    public void sendReminderSms(SmsReminderTemplate template){
        String message = "Reminder!!! that your reservation "+ template.transactionId()
                + "check-in date is tomorrow";
        twilioSmsSenderService.sendSms(
                new SmsNotification(
                        template.clientNumber(),
                        message
                )
        );
    }

}
