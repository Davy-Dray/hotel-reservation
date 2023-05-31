package com.ragnar.hotel_reservation.reservation.reservation_history;

import com.ragnar.hotel_reservation.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationHistoryServiceImpl implements  ReservationHistoryService{

    private final ReservationHistoryRepository historyRepository;

    public ReservationHistoryServiceImpl(ReservationHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public void createReservationHistory(ReservationHistory reservationHistory) {
        historyRepository.save(reservationHistory);
    }

    @Override
    public List<ReservationHistory> getAllReservationHistory() {
        return historyRepository.findAll();
    }

    @Override
    public ReservationHistory getReservationHistoryById(Long id) {
        Optional<ReservationHistory> optionalReservationHistory = historyRepository.findById(id);
        if (optionalReservationHistory.isEmpty()){
            throw new ResourceNotFoundException(
                    "history with id [%s] not found".formatted(id)
            );
        }
       return  optionalReservationHistory.get();
    }
}
