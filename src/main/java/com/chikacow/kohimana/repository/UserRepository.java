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


    @Query(value = "SELECT * FROM tbl_user WHERE username = :username AND is_active = true LIMIT 1",
            nativeQuery = true)
    public Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM tbl_user WHERE email = :email AND is_active = true LIMIT 1",
            nativeQuery = true)
    public Optional<User> findByEmail(@Param("email") String email);

//    @Query(value = "SELECT * FROM tbl_user WHERE username = :username AND is_active = true",
//            nativeQuery = true)
    public boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM tbl_user WHERE username = :username", nativeQuery = true)
    public Optional<User> getByUsername(@Param("username") String username);


    @Query(value = "SELECT tbl_user.* FROM  tbl_user " +
            "INNER JOIN tbl_user_has_role ON tbl_user.id = tbl_user_has_role.user_id " +
            "WHERE tbl_user.username = :username AND tbl_user_has_role.role_id = :role_id", nativeQuery = true)
    public Optional<User> findByUsernameAndRole(@Param("username") String username,@Param("role_id") Integer roleID);

    @Query(value = "SELECT tbl_user.* FROM tbl_user INNER JOIN tbl_staff " +
            "ON tbl_user.id = tbl_staff.user_id " +
            "WHERE tbl_staff.id = :staffId", nativeQuery = true)
    public Optional<User> findByStaffId(@Param("staffId") Long staffId);
}
