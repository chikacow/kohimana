package com.chikacow.kohimana.service;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.repository.SeatRepository;

import com.chikacow.kohimana.util.enums.TableStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearService {

    private final SeatRepository seatRepository;


    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id));
    }


    public Seat getSeatByTableNo(String tableNo) {
        return seatRepository.findByTableNo(tableNo)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with table number: " + tableNo));
    }


    public List<Seat> getSeatsByStatus(TableStatus status) {
        return seatRepository.findByStatus(status);
    }


    public Seat createSeat(Seat seat) {
        if (seatRepository.existsByTableNo(seat.getTableNo())) {
            throw new IllegalArgumentException("Table number already exists");
        }
        return seatRepository.save(seat);
    }

    public List<Seat> createBatchSeat(List<Seat> seats) {
        List<Seat> batchSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seatRepository.existsByTableNo(seat.getTableNo())) {
                throw new IllegalArgumentException("Table number already exists");
            }
             batchSeats.add(seatRepository.save(seat));
        }
        return batchSeats;
    }

    @Transactional
    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = getSeatById(id);

        if (!seat.getTableNo().equals(seatDetails.getTableNo())) {
            if (seatRepository.existsByTableNo(seatDetails.getTableNo())) {
                throw new IllegalArgumentException("Table number already exists");
            }
            seat.setTableNo(seatDetails.getTableNo());
        }

        seat.setDescription(seatDetails.getDescription());
        seat.setStatus(seatDetails.getStatus());

        return seatRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        Seat seat = getSeatById(id);
        seatRepository.delete(seat);
    }


    public Seat updateSeatStatus(Long id, TableStatus status) {
        Seat seat = getSeatById(id);
        seat.setStatus(status);
        return seatRepository.save(seat);
    }
}
