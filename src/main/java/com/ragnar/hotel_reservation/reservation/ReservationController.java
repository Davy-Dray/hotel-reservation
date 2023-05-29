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
    @GetMapping(path = "transaction/{transactionId}")
    public Reservation findReservationByTransactionId(
            @PathVariable("transactionId") String reservationId){
        return reservationService.findReservationByTransactionId(reservationId);
    }
    @GetMapping
    public List<Reservation> findAllReservations(){
        return reservationService.findAllReservations();
    }
    @PostMapping
    public void createReservation(@RequestBody ReservationRequest request){
        reservationService.createReservation(request);
    }
    @PutMapping(path = "{transactionId}")
    public void checkClientInOrOut(@PathVariable("transactionId")String transactionId ){
        reservationService.checkClientInOrOut(transactionId);
    }
    @GetMapping(path = "user/{userId}")
    public List<Reservation> findAllReservationsForUser(
            @PathVariable("userId") Long userId){
        return  reservationService.getAllReservationsForAUser(userId);
    }
    @DeleteMapping(path = "{reservationId}")
    public void delete(@PathVariable("reservationId") Long reservationId ){
        reservationService.deleteReservation(reservationId);
    }
    @PutMapping(path = {"{transactionId}/cancel"})
    public void cancelReservation(@PathVariable("transactionId")String transactionId){
        reservationService.cancelReservation(transactionId);
    }

}
