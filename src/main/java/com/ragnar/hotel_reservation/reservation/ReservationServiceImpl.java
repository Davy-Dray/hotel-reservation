package com.ragnar.hotel_reservation.reservation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements  ReservationService {

    private final ReservationRepository reservationRepository;
    @Override
    public Reservation findReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow();
    }
    @Override
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
    @Override
    public Reservation createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();




        return reservationRepository.save(reservation);
    }
    @Override
    public void updateReservationStatus(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        reservation.setHasCheckedIn(true);
        reservationRepository.save(reservation);
    }
}
