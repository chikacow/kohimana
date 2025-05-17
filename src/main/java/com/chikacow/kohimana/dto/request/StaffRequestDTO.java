package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import com.chikacow.kohimana.validator.EnumSubset;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StaffRequestDTO {

    @NotBlank(message = "Must include username")
    private String username;

    @NotNull(message = "Start date must not be blank")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate = LocalDate.now();

    @EnumSubset(enumClass = WorkingStatus.class)
    private String workingStatus;

    @EnumSubset(enumClass = StaffTeam.class)
    private String staffTeam;
}
