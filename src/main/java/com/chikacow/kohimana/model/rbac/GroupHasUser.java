package com.chikacow.kohimana.model.rbac;

import com.chikacow.kohimana.model.AbstractEntity;
import com.chikacow.kohimana.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_group_has_user")
@Getter
@Setter
@RequiredArgsConstructor
public class GroupHasUser extends AbstractEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_")
    private User user;
}
