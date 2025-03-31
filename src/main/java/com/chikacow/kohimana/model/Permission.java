package com.chikacow.kohimana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_permission")
@Getter
@Setter
@RequiredArgsConstructor
public class Permission extends AbstractEntity<Integer> {
    private String name;
    private String description;

    @OneToMany(mappedBy = "permission")
    private Set<RoleHasPermission> roleHasPermissions = new HashSet<>();
}
