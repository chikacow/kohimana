package com.chikacow.kohimana.model;

import com.chikacow.kohimana.util.enums.TableStatus;
import com.chikacow.kohimana.validator.EnumSubset;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "tbl_seat")
@Slf4j
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Seat extends AbstractEntity<Long> {

    @Column(name = "table_no", nullable = false, unique = true)
    private String tableNo;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.AVAILABLE;

    @PrePersist
    @PreUpdate
    private void prePersist() {

        this.tableNo = this.tableNo.trim().replaceAll("\\s+", "");;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class SeatRequestDTO {

        @NotBlank
        private String tableNo;

        private String description;

        @EnumSubset(enumClass = TableStatus.class)
        private String status;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SeatResponseDTO {

        private String tableNo;

        private String description;

        private TableStatus status;


    }



}