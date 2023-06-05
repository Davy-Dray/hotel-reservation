package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.exception.DuplicateResourceException;
import com.ragnar.hotel_reservation.exception.ReservationException;
import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import com.ragnar.hotel_reservation.notification.NotificationSenderService;
import com.ragnar.hotel_reservation.reservation.reservation_history.ReservationHistory;
import com.ragnar.hotel_reservation.reservation.reservation_history.ReservationHistoryService;
import com.ragnar.hotel_reservation.room.Room;
import com.ragnar.hotel_reservation.room.RoomService;
import com.ragnar.hotel_reservation.room.RoomStatus;
import com.ragnar.hotel_reservation.user.User;
import com.ragnar.hotel_reservation.user.UserService;
import com.ragnar.hotel_reservation.validation.InputValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final NotificationSenderService notificationSenderService;
    private final ReservationHistoryService reservationHistoryService;


    @Override
    public Reservation findReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findReservationById(id);
        if (reservation.isEmpty()) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        return reservation.get();
    }

    @Override
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public void createReservation(ReservationRequest reservationRequest) {
        LocalDate checkInDate = InputValidation.parseLocalDate(reservationRequest.checkInDate());
        LocalDate checkOutDate = InputValidation.parseLocalDate(reservationRequest.checkOutDate());
        InputValidation.validateReservationDates(checkInDate, checkOutDate);

        if (isReservationAllowed(reservationRequest.roomId(), checkInDate, checkOutDate)) {
            throw new DuplicateResourceException(
                    """
                            Room is not available for the specified dates.\s
                            Please pick other dates or another room
                            """
            );
        }
        User user = userService.findUserById(reservationRequest.userId());
        Room room = roomService.findRoomById(reservationRequest.roomId());

        Reservation reservation = new Reservation(
                checkInDate, checkOutDate, user, generateTransactionId(), room
        );
        reservationRepository.save(reservation);
        ReservationHistory reservationHistory = new ReservationHistory(
                reservation,
                "RESERVATION"
        );
        reservationHistoryService.createReservationHistory(reservationHistory);

//        notificationSenderService.sendBookingConfirmationSms(
//                new SmsConfirmationTemplate(
//                        user.getFirstname(),
//                        user.getPhoneNumber(),
//                        checkInDate,
//                        room.getRoomType().toString(),
//                        reservation.getTransactionId(),
//                        reservation.getReservedRoom().getRoomNumber()
//                )
//        );
    }


    @Override
    public void checkClientIn(String transactionId) {
        Reservation reservation = findReservationByTransactionId(transactionId);
        boolean hasCheckedIn = reservation.isHasCheckedIn();
        ReservationStatus reservationStatus = reservation.getStatus();

        Room reservedRoom = reservation.getReservedRoom();
        if (!hasCheckedIn && reservationStatus == ReservationStatus.PENDING) {
            reservation.setHasCheckedIn(true);
            reservation.setStatus(ReservationStatus.ACTIVE);
            reservedRoom.setRoomStatus(RoomStatus.OCCUPIED);
        } else {
            throw new ReservationException(
                    "reservation %s ".formatted(reservation.getStatus())
            );
        }
        reservationRepository.save(reservation);
        ReservationHistory reservationHistory = new ReservationHistory(
                reservation,
                "CHECK-IN"
        );
        reservationHistoryService.createReservationHistory(reservationHistory);
    }

    @Override
    public void checkClientOut(String reservationId) {
        Reservation reservation = findReservationByTransactionId(reservationId);
        Room reservedRoom = reservation.getReservedRoom();
        ReservationStatus reservationStatus = reservation.getStatus();

        if (reservationStatus == ReservationStatus.ACTIVE) {
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservedRoom.setRoomStatus(RoomStatus.AVAILABLE);
        } else {
            throw new ReservationException(
                    "reservation is %s ".formatted(reservation.getStatus())
            );
        }
        reservationRepository.save(reservation);
        ReservationHistory reservationHistory = new ReservationHistory(
                reservation,
                "CHECK-OUT"
        );
        reservationHistoryService.createReservationHistory(reservationHistory);
    }

    @Override
    public boolean existsReservationById(Long id) {
        return !reservationRepository.existsById(id);
    }

    @Override
    public List<Reservation> getAllReservationsForAUser(Long id) {
        return reservationRepository.findReservationsByUserId(id);
    }

    @Override
    public void deleteReservation(Long id) {
        if (existsReservationById(id)) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation findReservationByTransactionId(String transactionId) {
        Optional<Reservation> reservation =
                reservationRepository.findReservationsByTransactionId(transactionId);
        if (reservation.isEmpty()) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(transactionId)
            );
        }
        return reservation.get();
    }

    @Override
    public Reservation findReservationByRoomNumber(int id) {
        Optional<Reservation> reservation =
                reservationRepository.findReservationsByReservedRoom_RoomNumber(id);
        if (reservation.isEmpty()) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        return reservation.get();
    }

    @Override
    public List<Room> findAllReservedRooms() {
        return reservationRepository.findAllReservedRooms();
    }

    @Override
    public void cancelReservation(String transactionId) {
        Optional<Reservation> optionalReservation =
                reservationRepository.findReservationsByTransactionId(transactionId);

        if (optionalReservation.isEmpty()) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(transactionId)
            );
        }
        Reservation reservation = optionalReservation.get();
        ReservationStatus reservationStatus = reservation.getStatus();
        if (reservationStatus == ReservationStatus.PENDING) {
            reservation.setStatus(ReservationStatus.CANCELLED);

        } else {
            throw new ReservationException(
                    "reservation %s ".formatted(reservation.getStatus().name())
            );
        }
        reservationRepository.save(reservation);
        ReservationHistory reservationHistory = new ReservationHistory(
                reservation,
                "CANCELLATION"
        );
        reservationHistoryService.createReservationHistory(reservationHistory);
//        notificationSenderService.sendBookingCancellationSms(
//                new SmsCancellationTemplate(
//                        reservation.getUser().getFirstname(),
//                        reservation.getUser().getPhoneNumber(),
//                        reservation.getTransactionId()
//                )
//        );
    }

    @Override
    @Transactional
    public void updateReservation(Long id, ReservationUpdateRequest updateRequest) {
        if (existsReservationById(id)) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        Reservation reservation = findReservationById(id);
        ReservationStatus reservationStatus = reservation.getStatus();
        if (reservationStatus != ReservationStatus.PENDING) {

            throw new ReservationException(
                    "reservation is %s ".formatted(reservation.getStatus().name())
            );
        }

        LocalDate checkInDate = InputValidation.parseLocalDate(updateRequest.checkInDate());
        LocalDate checkOutDate = InputValidation.parseLocalDate(updateRequest.checkOutDate());
        InputValidation.validateReservationDates(checkInDate, checkOutDate);

        reservation.setStatus(ReservationStatus.CANCELLED);

        if (isReservationAllowed(reservation.getReservedRoom().getId(), checkInDate, checkOutDate)) {
            throw new DuplicateResourceException(
                           """
                            Room is not available for the specified dates.
                            Please pick other dates or another room
                             """
            );
        }

        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setStatus(ReservationStatus.PENDING);

        reservationRepository.save(reservation);
        ReservationHistory reservationHistory = new ReservationHistory(
                reservation,
                "UPDATE"
        );
        reservationHistoryService.createReservationHistory(reservationHistory);
    }


    private String generateTransactionId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int transactionId;
        boolean exists;
        do {
            transactionId = random.nextInt(1_000_000_000);
            exists = reservationRepository.existsByTransactionId(
                    String.format(
                            "%09d",
                            transactionId
                    ));
        } while (exists);
        return String.format("%09d", transactionId);
    }

    private boolean isReservationAllowed(
            Long roomId, LocalDate checkInDate, LocalDate checkOutDate
    ) {
        List<Reservation> overlappingReservations =
                reservationRepository
                        .findOverlappingReservationsForRoom(
                                roomId,
                                checkInDate,
                                checkOutDate
                        );
        return !overlappingReservations.isEmpty();
    }
}
