package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.rbac.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Long> {
}
