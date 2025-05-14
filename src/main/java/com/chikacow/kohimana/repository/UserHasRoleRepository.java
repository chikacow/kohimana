package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.rbac.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Integer> {
    @Query(value = "SELECT tbl_user_has_role.* FROM tbl_user_has_role " +
            " JOIN tbl_user ON tbl_user_has_role.user_id = tbl_user.id " +
            " JOIN tbl_role ON tbl_user_has_role.role_id = tbl_role.id " +
            " WHERE tbl_role.id = :roleId AND tbl_user.id = :userId ", nativeQuery = true)
    UserHasRole findByUserIdAndRoleName(@Param("userId") Long userId, @Param("roleId") Integer roleId);


    @Modifying
    @Query(value = "DELETE From tbl_user_has_role WHERE tbl_user_has_role.id = :id", nativeQuery = true)
    public void deleteUhr(@Param("id") Integer id);
}
