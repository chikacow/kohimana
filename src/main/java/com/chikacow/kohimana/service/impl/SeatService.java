package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.mapper.SeatMapper;
import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.repository.SeatRepository;

import com.chikacow.kohimana.util.enums.TableStatus;
import jakarta.persistence.EntityManager;
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
        return SeatMapper.fromEntityToResponseDTO(getSeatById(id));

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
        return seatRepository.findByStatus(status).stream().map(SeatMapper::fromEntityToResponseDTO).toList();
    }

    /**
     * Store data of new seats, rarely use this
     * @param requestDTO
     * @return
     */
    @Transactional
    public Seat.SeatResponseDTO createSeat(Seat.SeatRequestDTO requestDTO) {
        isSeatAlreadyExists(requestDTO);

        Seat seat = SeatMapper.fromRequestDTOToEntity(requestDTO);

        entityManager.persist(seat);

        return SeatMapper.fromEntityToResponseDTO(seat);
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
            isSeatAlreadyExists(req);
            Seat saved = SeatMapper.fromRequestDTOToEntity(req);
            seatRepository.save(saved);
            Seat.SeatResponseDTO res = SeatMapper.fromEntityToResponseDTO(saved);
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
        Seat seat = getSeatById(id);

        if (!seat.getTableNo().equals(requestDTO.getTableNo())) {
            isSeatAlreadyExists(requestDTO);
            seat.setTableNo(requestDTO.getTableNo());
        }
        seat.setDescription(requestDTO.getDescription());
        seat.setStatus(TableStatus.fromString(requestDTO.getStatus()));

        return SeatMapper.fromEntityToResponseDTO(seat);
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
    @Transactional
    public Seat.SeatResponseDTO updateSeatStatus(Long id, String status) {
        Seat seat = getSeatById(id);
        seat.setStatus(TableStatus.fromString(status));
        seatRepository.save(seat);

        return SeatMapper.fromEntityToResponseDTO(seat);
    }

    private void isSeatAlreadyExists(Seat.SeatRequestDTO requestDTO) {
        if (seatRepository.existsByTableNo(requestDTO.getTableNo())) {
            throw new IllegalArgumentException("Table number already exists");
        }
    }
}
