package com.chikacow.kohimana.model.rbac;

import com.chikacow.kohimana.model.AbstractEntity;
import com.chikacow.kohimana.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_user_has_role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserHasRole extends AbstractEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
