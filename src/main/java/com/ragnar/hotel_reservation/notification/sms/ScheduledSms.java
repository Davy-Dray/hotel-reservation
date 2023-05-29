package com.ragnar.hotel_reservation.notification.sms;


import com.ragnar.hotel_reservation.notification.sms.twilio.TwilioSmsSenderService;
import com.ragnar.hotel_reservation.reservation.Reservation;
import com.ragnar.hotel_reservation.reservation.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class ScheduledSms {

    private final TwilioSmsSenderService twilioSmsSenderService;
    private final ReservationService reservationService;
   // @Scheduled(cron= "0 0/30 8-13 * * *")

    @Scheduled(cron = "0 * * * * *")
    public void checkReservations() {
        List<Reservation> allReservations = reservationService.findAllReservations();
        LocalDate currentDate = LocalDate.now();

        for (Reservation reservation : allReservations) {
            if (!reservation.isHasCheckedIn()) {
                LocalDate checkInDate = reservation.getCheckInDate();
                long daysDifference = ChronoUnit.DAYS.between(currentDate, checkInDate);

                if (daysDifference >= 1) {
                   // sendReminderNotification(reservation);
                    notifyMe();
                }
            }
        }
    }

    private void sendReminderNotification(Reservation reservation) {
        String phoneNumber = reservation.getUser().getPhoneNumber();
        String message = "Reminder that your reservation " +reservation.getTransactionId() +"check-in date is tomorrow";
        twilioSmsSenderService.sendSms(new SmsNotification(phoneNumber, message));
    }

    private void notifyMe(){
        System.out.println("notify");
    }
}
