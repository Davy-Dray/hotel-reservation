package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.exception.DuplicateResourceException;
import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import com.ragnar.hotel_reservation.exception.ValidationException;
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

        if (!isReservationAllowed(reservationRequest.roomId(), checkInDate, checkOutDate)) {
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
    public void checkClientInOrOut(String transactionId) {
        Reservation reservation = findReservationByTransactionId(transactionId);

        boolean hasCheckedIn = reservation.isHasCheckedIn();
        Room reservedRoom = reservation.getReservedRoom();
        if (!hasCheckedIn) {
            reservation.setHasCheckedIn(true);
            reservedRoom.setRoomStatus(RoomStatus.OCCUPIED);
        } else {
            reservation.setHasCheckedIn(false);
            reservedRoom.setRoomStatus(RoomStatus.AVAILABLE);
        }
        reservationRepository.save(reservation);
    }


    @Override
    public boolean existsReservationById(Long id) {
        return reservationRepository.existsById(id);
    }

    @Override
    public List<Reservation> getAllReservationsForAUser(Long id) {
        return reservationRepository.findReservationsByUserId(id);
    }

    @Override
    public void deleteReservation(Long id) {
        if (!existsReservationById(id)) {
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
        return overlappingReservations.isEmpty();
    }
}
