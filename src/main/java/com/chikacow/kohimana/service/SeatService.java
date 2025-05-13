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

/**
 * A simple service for managing spaces in the store
 */

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;


    /**
     * Get all position for customer to seat
     * @return
     */
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    /**
     * Get by seat id in the database
     * @param id
     * @return
     */
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id));
    }

    /**
     * Get seat by its number or nicknames in the store
     */
    public Seat getSeatByTableNo(String tableNo) {
        return seatRepository.findByTableNo(tableNo)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with table number: " + tableNo));
    }


    /**
     * Filtering seats by its status like cleaning/occupied/ready/ etc
     * @param status
     * @return
     */
    public List<Seat> getSeatsByStatus(TableStatus status) {
        return seatRepository.findByStatus(status);
    }

    /**
     * Store data of new seats, rarely use this
     * @param seat
     * @return
     */
    public Seat createSeat(Seat seat) {
        if (seatRepository.existsByTableNo(seat.getTableNo())) {
            throw new IllegalArgumentException("Table number already exists");
        }
        return seatRepository.save(seat);
    }

    /**
     * Use when you want to add 10 seats at a time
     * @param seats
     * @return
     */
    @Transactional
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

    /**
     * Update seat details, can set status as well but not the best practice to do so
     * @param id
     * @param seatDetails
     * @return
     */
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

    /**
     * Delete the seat from the system
     * @param id
     */
    public void deleteSeat(Long id) {
        Seat seat = getSeatById(id);
        seatRepository.delete(seat);
    }


    /**
     * Change status of the seat so the customer can know where they can occupy
     * @param id
     * @param status
     * @return
     */
    public Seat updateSeatStatus(Long id, TableStatus status) {
        Seat seat = getSeatById(id);
        seat.setStatus(status);
        return seatRepository.save(seat);
    }
}
