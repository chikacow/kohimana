package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query(value = "SELECT tbl_staff.* FROM tbl_staff INNER JOIN tbl_user " +
            "ON tbl_staff.user_id = tbl_user.id " +
            "WHERE tbl_user.username = :username ", nativeQuery = true)
    public Optional<Staff> findByUserUsername(@Param("username") String username);
}
