package com.chikacow.kohimana.service;

import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Managing role, may growth more in next versions
 * Current the CRUD is done right through the database by running initial script
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
