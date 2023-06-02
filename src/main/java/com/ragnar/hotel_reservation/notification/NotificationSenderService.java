package com.ragnar.hotel_reservation.notification;

import com.ragnar.hotel_reservation.notification.sms.SmsNotification;
import com.ragnar.hotel_reservation.notification.sms.template.SmsCancellationTemplate;
import com.ragnar.hotel_reservation.notification.sms.template.SmsConfirmationTemplate;
import com.ragnar.hotel_reservation.notification.sms.template.SmsReminderTemplate;
import com.ragnar.hotel_reservation.notification.sms.twilio.TwilioSmsSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationSenderService {

    private final TwilioSmsSenderService twilioSmsSenderService;


    public void sendBookingConfirmationSms(SmsConfirmationTemplate template) {
        String message =
                """
                Dear %s, \s
                Your booking has been confirmed for %s. Your booking ID is %s.
                Room Type- %s.
                Room number- %s
                """;
        message = String.format(
                message,
                template.clientName(),
                template.checkInDate(),
                template.transactionId(),
                template.roomType(),
                template.roomNo()
        );
        twilioSmsSenderService.sendSms(new SmsNotification(
                template.clientNumber(),
                message
        ));

    }

    public void sendBookingCancellationSms(SmsCancellationTemplate template) {
        String message =
                """
                Dear %s,
                Your booking %s has been cancelled.
                """;
        message = String.format(message, template.clientName(), template.transactionId());

        twilioSmsSenderService.sendSms(
                new SmsNotification(
                        template.clientNumber(),
                        message
                )
        );
    }

    public void sendReminderSms(SmsReminderTemplate template) {
        String message =
                """
                Reminder!!! that your reservation %s
                check-in date is tomorrow,
                after tomorrow if not checked-in reservation will be cancelled.
                You can visit our site to update your check-in date
                """;
        message = String.format(message, template.transactionId());
        twilioSmsSenderService.sendSms(
                new SmsNotification(
                        template.clientNumber(),
                        message
                )
        );
    }

}
