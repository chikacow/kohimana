package com.chikacow.kohimana.model;

import com.chikacow.kohimana.util.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_attendance")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Attendance extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "status")
    private AttendanceStatus status;

    @Column(name = "note")
    private String note;

}
