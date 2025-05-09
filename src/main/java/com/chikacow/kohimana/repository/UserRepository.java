package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = "SELECT * FROM tbl_user WHERE username = :username AND is_active = true",
            nativeQuery = true)
    public Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM tbl_user WHERE email = :email AND is_active = true",
            nativeQuery = true)
    public Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM tbl_user WHERE username = :username AND is_active = true",
            nativeQuery = true)
    public boolean existsByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM tbl_user WHERE username = :username", nativeQuery = true)
    public Optional<User> getByUsername(@Param("username") String username);
}
