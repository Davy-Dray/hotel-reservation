package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.exception.DuplicateResourceException;
import com.ragnar.hotel_reservation.exception.ReservationException;
import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import com.ragnar.hotel_reservation.exception.ValidationException;
import com.ragnar.hotel_reservation.notification.NotificationSenderService;
import com.ragnar.hotel_reservation.room.Room;
import com.ragnar.hotel_reservation.room.RoomService;
import com.ragnar.hotel_reservation.room.RoomStatus;
import com.ragnar.hotel_reservation.user.User;
import com.ragnar.hotel_reservation.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomService roomService;
    private  final NotificationSenderService notificationSenderService;

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
        LocalDate checkInDate = parseLocalDate(reservationRequest.checkInDate());
        LocalDate checkOutDate = parseLocalDate(reservationRequest.checkOutDate());
        validateReservationDates(checkInDate, checkOutDate);

        if (isReservationAllowed(reservationRequest.roomId(), checkInDate, checkOutDate)) {
            throw new DuplicateResourceException(
                    "Room is not available for the specified dates. Please choose another date or another room."
            );
        }

        User user = userService.findUserById(reservationRequest.userId());
        Room room = roomService.findRoomById(reservationRequest.roomId());

        Reservation reservation = new Reservation(
                checkInDate, checkOutDate, user, generateTransactionId(), room
        );

        reservationRepository.save(reservation);

//        notificationSenderService.sendBookingConfirmationSms(
//                new  SmsTemplate(
//                        user.getFirstname(),
//                        user.getPhoneNumber(),
//                        checkInDate,
//                        room.getRoomType().toString(),
//                        reservation.getTransactionId()
//                )
//        );
    }

    private LocalDate parseLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(date, formatter);
    }

    private void validateReservationDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (!checkInDate.isBefore(checkOutDate)) {
            throw new ValidationException("Check-in date must be before check-out date.");
        }
    }
    @Override
    public void checkClientIn(String transactionId) {
        Reservation reservation = findReservationByTransactionId(transactionId);
        boolean hasCheckedIn = reservation.isHasCheckedIn();
        Room reservedRoom = reservation.getReservedRoom();
        if (!hasCheckedIn && reservation.getStatus()==ReservationStatus.PENDING) {
            reservation.setHasCheckedIn(true);
            reservation.setStatus(ReservationStatus.ACTIVE);
            reservedRoom.setRoomStatus(RoomStatus.OCCUPIED);
        }else{
           throw new ReservationException(
                   "reservation [%s] ".formatted(reservation.getStatus().name())
           );
        }
        reservationRepository.save(reservation);
    }

    @Override
    public void checkClientOut(String reservationId) {
        Reservation reservation = findReservationByTransactionId(reservationId);
        Room reservedRoom = reservation.getReservedRoom();
        if (reservation.getStatus()==ReservationStatus.ACTIVE) {
            reservation.setHasCheckedIn(true);
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservedRoom.setRoomStatus(RoomStatus.AVAILABLE);
        }else{
            throw new ReservationException(
                    "reservation [%s] ".formatted(reservation.getStatus().name())
            );
        }
        reservationRepository.save(reservation);
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
    public Reservation findReservationByTransactionId(String id) {
        Optional<Reservation> reservation = reservationRepository.findReservationsByTransactionId(id);
        if (reservation.isEmpty()) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        return reservation.get();
    }

    @Override
    public Reservation findReservationByRoomNumber(int id) {
        Optional<Reservation> reservation = reservationRepository.findReservationsByReservedRoom_RoomNumber(id);
        if (reservation.isEmpty()) {
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        return reservation.get();
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
        if(reservation.getStatus()==ReservationStatus.PENDING){
            reservation.setStatus(ReservationStatus.CANCELLED);
        }else {
            throw new ReservationException(
                    "reservation [%s] ".formatted(reservation.getStatus().name())
            );
        }
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Long id, ReservationUpdateRequest updateRequest) {

        if(existsReservationById(id)){
            throw new ResourceNotFoundException(
                    "reservation with id [%s] not found".formatted(id)
            );
        }
        Reservation reservation = findReservationById(id);
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        LocalDate checkInDate = parseLocalDate(updateRequest.checkInDate());
        LocalDate checkOutDate = parseLocalDate(updateRequest.checkOutDate());
        validateReservationDates(checkInDate, checkOutDate);

        if (isReservationAllowed(reservation.getReservedRoom().getId(), checkInDate, checkOutDate)) {
            throw new DuplicateResourceException(
                    "Room is not available for the specified dates." +
                            " Please choose another date or another room."
            );
        }

        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservationRepository.save(reservation);
    }

    private String generateTransactionId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int transactionId;
        boolean exists;
        do {
            transactionId = random.nextInt(1_000_000_000);
            exists = reservationRepository.existsByTransactionId(String.format("%09d", transactionId));
        } while (exists);
        return String.format("%09d", transactionId);
    }

    private boolean isReservationAllowed(Long roomId,LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> overlappingReservations = reservationRepository
                .findOverlappingReservationsForRoom(roomId,checkInDate, checkOutDate);
        String s = "";
        return !overlappingReservations.isEmpty();
    }
}
