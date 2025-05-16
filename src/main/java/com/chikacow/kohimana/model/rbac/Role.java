package com.chikacow.kohimana.model.rbac;

import com.chikacow.kohimana.model.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_role")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Role extends AbstractEntity<Integer> {
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;


    @OneToMany(mappedBy = "role")
    private Set<RoleHasPermission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<UserHasRole> roles = new HashSet<>();


}
