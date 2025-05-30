package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.util.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByTableNo(String tableNo);

    @Query(value = "SELECT * FROM tbl_seat WHERE status = :status ", nativeQuery = true)
    List<Seat> findByStatus(@Param("status") String status);
    boolean existsByTableNo(String tableNo);
}