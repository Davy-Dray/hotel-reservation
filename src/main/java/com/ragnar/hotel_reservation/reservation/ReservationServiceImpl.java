package com.ragnar.hotel_reservation.reservation;

import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import com.ragnar.hotel_reservation.room.Room;
import com.ragnar.hotel_reservation.room.RoomService;
import com.ragnar.hotel_reservation.room.RoomStatus;
import com.ragnar.hotel_reservation.user.User;
import com.ragnar.hotel_reservation.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements  ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomService roomService;
    @Override
    public Reservation findReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findReservationById(id);
        if(reservation.isEmpty()){
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

        User user = userService.findUserById(reservationRequest.userId());
        Room room = roomService.findRoomById(reservationRequest.roomId());
        Reservation reservation = new Reservation(
                reservationRequest.checkInDate(),
                reservationRequest.checkOutDate(),
                user,
                generateTransactionId(),
                room
        );
        room.setRoomStatus(RoomStatus.OCCUPIED);
        reservationRepository.save(reservation);
    }
    @Override
    public void checkClientInOrOut(long reservationId) {
        Reservation reservation = findReservationById(reservationId);
        boolean checkedInStatus = reservation.isHasCheckedIn();
        reservation.setHasCheckedIn(checkedInStatus);
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
      if(!existsReservationById(id)){
          throw new ResourceNotFoundException(
                  "reservation with id [%s] not found".formatted(id)
          );
      }
      reservationRepository.deleteById(id);
    }

    //TODO:generate TransactionId
    private String generateTransactionId(){
        return "";
    }
}
