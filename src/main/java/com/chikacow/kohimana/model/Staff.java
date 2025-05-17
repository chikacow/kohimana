package com.chikacow.kohimana.model;

import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * in this version, each staff con working at only one team, and their level are all the same.
 * the difference between an intern and a senior can be noticed in the salary table
 */

@Entity
@Table(name = "tbl_staff")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Staff extends AbstractEntity<Long> {

    //@ManyToOne for working in many team
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_from")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @Column(name = "end_since")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate = null;

    @Column(name = "working_status")
    @Enumerated(EnumType.STRING)
    private WorkingStatus workingStatus;

    @Column(name = "team")
    @Enumerated(EnumType.STRING)
    private StaffTeam staffTeam;
}
