package com.chikacow.kohimana.model;

import com.chikacow.kohimana.model.rbac.GroupHasUser;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.util.enums.Gender;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity<Long> implements Serializable, UserDetails {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * bug
     */
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private Set<GroupHasUser> users = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserHasRole> roles = new HashSet<>();



    public void addRole(UserHasRole role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
        role.setUser(this); // cập nhật quan hệ 2 chiều
    }

    /**
     * get predefined authorities object from my designed rbac
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roleList = roles.stream().map(UserHasRole::getRole).toList();

        List<String> roleName = roleList.stream().map(Role::getName).toList();

        return roleName.stream().map(SimpleGrantedAuthority::new).toList();

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true ;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
