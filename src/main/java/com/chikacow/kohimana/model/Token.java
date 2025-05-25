package com.chikacow.kohimana.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_token")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Token extends AbstractEntity<Integer> {

    @Column(name = "username")
    private String username;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "reset_token")
    private String resetToken;
}