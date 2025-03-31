package com.chikacow.kohimana.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_role_has_permisstion")
@Getter
@Setter
@RequiredArgsConstructor
public class RoleHasPermission extends AbstractEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
