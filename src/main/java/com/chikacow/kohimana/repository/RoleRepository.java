package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select r from Role r inner join UserHasRole ur  on r.id = ur.user.id where ur.user.id =: userId")
    List<Role> getAllUserById(Long userId);
}

