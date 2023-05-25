package com.ragnar.hotel_reservation.reservation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/reservations/")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping(path = "{reservationId}")
    public Reservation findReservationById(
            @PathVariable("reservationId") Long reservationId){
        return reservationService.findReservationById(reservationId);
    }
    @GetMapping
    public List<Reservation> findAllReservations(){
        return reservationService.findAllReservations();
    }
    @PostMapping
    public void createReservation(@RequestBody ReservationRequest request){
        reservationService.createReservation(request);
    }

}
