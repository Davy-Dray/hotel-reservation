package com.ragnar.hotel_reservation.notification.sms;

import com.ragnar.hotel_reservation.notification.NotificationSenderService;
import com.ragnar.hotel_reservation.notification.sms.template.SmsReminderTemplate;
import com.ragnar.hotel_reservation.notification.sms.twilio.TwilioSmsSenderService;
import com.ragnar.hotel_reservation.reservation.Reservation;
import com.ragnar.hotel_reservation.reservation.ReservationRepository;
import com.ragnar.hotel_reservation.reservation.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class ScheduledSms {

    private final NotificationSenderService notificationSenderService;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    /**
     * sends notification to users whose check in date is the next day
     */
    // @Scheduled(cron= "0 0/30 8-13 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void sendBookingReminderSms() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Reservation> allReservations =
                reservationRepository.findByCheckInDateTomorrow(tomorrow);

        for (Reservation reservation : allReservations) {
            if (!reservation.isHasCheckedIn()) {
                System.out.println("notify");
                //   sendReminderNotification(reservation);
            }
        }
    }

    private void sendReminderNotification(Reservation reservation) {
       notificationSenderService.sendReminderSms(
              new SmsReminderTemplate(
                      reservation.getUser().getPhoneNumber(),
                      reservation.getTransactionId()
              )
       );
    }

    /**
     *cancels reservations
     * sends notification to users whose reservations where cancelled
     */
    // @Scheduled(cron= "0 0/30 8-13 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void cancelStaleReservations() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Reservation> staleReservations =
                reservationRepository.findReservationsByCheckInDateAndStatus(yesterday);
        for (Reservation reservation : staleReservations) {
            reservationService.cancelReservation(reservation.getTransactionId());
        }

    }
}
