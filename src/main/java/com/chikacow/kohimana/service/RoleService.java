package com.chikacow.kohimana.service;

import com.chikacow.kohimana.exception.DatabaseException;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.repository.RoleRepository;
import com.chikacow.kohimana.util.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Role role = roleRepository.findByName(name);
        if (role == null) {
            throw new DatabaseException("Role not found from database, potentially database initialize problem");
        }
        return role;
    }

    public void addRoleToUser(User user, RoleEnum roleEnum) {
        Role role = getRoleByName(RoleEnum.convertToString(roleEnum));

        UserHasRole userRole = UserHasRole.builder()
                .role(role)
                .user(user)
                .build();
        user.addRole(userRole);

    }
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void addManyRoleToUserSlow(User user, List<String> roles) {

        List<Role> listRole = new ArrayList<>();
        for (String r : roles) {
            //fetch from db
            Role re = getRoleByName(r);
            listRole.add(re);
        }

        for (Role r : listRole) {
            UserHasRole userRole = UserHasRole.builder()
                    .role(r)
                    .user(user)
                    .build();

            user.addRole(userRole);
        }
    }

    public void addManyRoleToUser(User user, List<RoleEnum> roles) {
        List<Role> allRoles = getAllRoles();
        List<Role> roleList = allRoles.stream()
                .filter(role -> roles.contains(RoleEnum.fromString(role.getName())))
                .toList();

        for (Role r : roleList) {
            UserHasRole userRole = UserHasRole.builder()
                    .role(r)
                    .user(user)
                    .build();

            user.addRole(userRole);
        }
    }
}
