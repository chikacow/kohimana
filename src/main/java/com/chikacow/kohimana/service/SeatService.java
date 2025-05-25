package com.chikacow.kohimana.service;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.repository.SeatRepository;

import com.chikacow.kohimana.util.enums.TableStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple service for managing spaces in the store
 */

@Service
@Slf4j(topic = "SEAT-SERVICE")
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @PersistenceContext
    private final EntityManager entityManager;


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

    public Seat.SeatResponseDTO getSeatInfo(Long id) {
        Seat seat = getSeatById(id);
        return Seat.SeatResponseDTO.builder()
                .tableNo(seat.getTableNo())
                .description(seat.getDescription())
                .status(seat.getStatus())
                .build();

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
    public List<Seat.SeatResponseDTO> getSeatsByStatus(String status) {
        List<Seat> fetched = seatRepository.findByStatus(status);

        return fetched.stream().map(seat -> Seat.SeatResponseDTO.builder()
                .tableNo(seat.getTableNo())
                .description(seat.getDescription())
                .status(seat.getStatus())
                .build()).toList();
    }

    /**
     * Store data of new seats, rarely use this
     * @param requestDTO
     * @return
     */
    @Transactional
    public Seat.SeatResponseDTO createSeat(Seat.SeatRequestDTO requestDTO) {
        if (seatRepository.existsByTableNo(requestDTO.getTableNo())) {
            throw new IllegalArgumentException("Table number already exists");
        }

        Seat saved = Seat.builder()
                .tableNo(requestDTO.getTableNo())
                .description(requestDTO.getDescription())
                .status(TableStatus.fromString(requestDTO.getStatus()))
                .build();

        //move saved to persistent context, new object -> prepare a persist()
        entityManager.persist(saved);


        log.info("create");
        Seat.SeatResponseDTO res = Seat.SeatResponseDTO.builder()
                .tableNo(saved.getTableNo())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .build();

        log.info("create completed");
        //auto flush()
        return res;
    }

    /**
     * Use when you want to add 10 seats at a time
     * @param requestDTOS
     * @return
     */
    @Transactional
    public List<Seat.SeatResponseDTO> createBatchSeat(List<Seat.SeatRequestDTO> requestDTOS) {
        List<Seat.SeatResponseDTO> batchSeats = new ArrayList<>();
        for (Seat.SeatRequestDTO req : requestDTOS) {
            if (seatRepository.existsByTableNo(req.getTableNo())) {
                throw new IllegalArgumentException("Table number already exists");
            }
            Seat saved = Seat.builder()
                    .tableNo(req.getTableNo())
                    .description(req.getDescription())
                    .status(TableStatus.fromString(req.getStatus()))
                    .build();
            seatRepository.save(saved);
            Seat.SeatResponseDTO res = Seat.SeatResponseDTO.builder()
                    .tableNo(saved.getTableNo())
                    .description(saved.getDescription())
                    .status(saved.getStatus())
                    .build();
            batchSeats.add(res);
        }
        return batchSeats;
    }

    /**
     * Update seat details, can set status as well but not the best practice to do so
     * @param id
     * @param requestDTO
     * @return
     */
    @Transactional
    public Seat.SeatResponseDTO updateSeat(Long id, Seat.SeatRequestDTO requestDTO) {
        Seat seat = getSeatById(id); //which called

        if (!seat.getTableNo().equals(requestDTO.getTableNo())) {
            if (seatRepository.existsByTableNo(requestDTO.getTableNo())) {
                throw new IllegalArgumentException("Table number already exists");
            }
            seat.setTableNo(requestDTO.getTableNo());
        }

        seat.setDescription(requestDTO.getDescription());
        seat.setStatus(TableStatus.fromString(requestDTO.getStatus()));

        /**
         * bug, this should be automatically called but i have to do it manually to achieve something obvious
         */
        //entityManager.flush();


        log.info("update");

        var res = Seat.SeatResponseDTO.builder()
                .tableNo(seat.getTableNo())
                .description(seat.getDescription())
                .status(seat.getStatus())
                .build();
        log.info("update complete");
        return res;
    }

    /**
     * Delete the seat from the system
     * @param id
     */
    public String deleteSeat(Long id) {
        Seat seat = getSeatById(id);
        seat.setActive(!seat.isActive());
        seatRepository.save(seat);
        return seat.isActive() ? "true" : "false";

    }


    /**
     * Change status of the seat so the customer can know where they can occupy
     * @param id
     * @param status
     * @return
     */
    public Seat.SeatResponseDTO updateSeatStatus(Long id, String status) {
        Seat seat = getSeatById(id);
        seat.setStatus(TableStatus.fromString(status));
        Seat saved = seatRepository.save(seat);

        Seat.SeatResponseDTO res = Seat.SeatResponseDTO.builder()
                .tableNo(saved.getTableNo())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .build();

        return res;
    }
}
