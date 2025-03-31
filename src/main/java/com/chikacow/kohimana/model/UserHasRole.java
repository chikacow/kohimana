package com.chikacow.kohimana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_user_has_role")
@Getter
@Setter
@RequiredArgsConstructor
public class UserHasRole extends AbstractEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
