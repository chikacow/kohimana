package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.util.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByTableNo(String tableNo);
    List<Seat> findByStatus(TableStatus status);
    boolean existsByTableNo(String tableNo);
}