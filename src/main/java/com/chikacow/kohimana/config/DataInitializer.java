package com.chikacow.kohimana.config;

import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.repository.RoleRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.RoleService;
import com.chikacow.kohimana.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {

        initRoles();
        initAdmin("sysadmin", "password");



    }

    private void initRoles() {
        List<String> lR = List.of("ADMIN", "MANAGER", "STAFF", "CUSTOMER");
        for (String roleName : lR) {

            boolean exists = ((Number) entityManager.createNativeQuery(
                            "SELECT COUNT(*) FROM tbl_role WHERE name = ?")
                    .setParameter(1, roleName)
                    .getSingleResult()).longValue() > 0;

            if (!exists) {
                entityManager.persist(Role.builder()
                        .name(roleName).build());
            }
        }
    }

    private void initAdmin(String adminUsername, String adminPassword) {
        boolean exists = ((Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM tbl_user WHERE username = ?")
                .setParameter(1, adminUsername)
                .getSingleResult()).longValue() > 0;

        if (!exists) {
            entityManager.persist(User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .isActive(true)
                    .build());

            User res = (User) entityManager.createNativeQuery("SELECT * FROM tbl_user WHERE username = ?", User.class)
                    .setParameter(1, adminUsername)
                    .getSingleResult();


            Role role = (Role) entityManager.createNativeQuery("SELECT * FROM tbl_role WHERE name = ?", Role.class)
                    .setParameter(1, "ADMIN")
                    .getSingleResult();


            UserHasRole uhr = UserHasRole.builder()
                    .user(res)
                    .role(role)
                    .build();

            entityManager.persist(uhr);
        }
    }
}

