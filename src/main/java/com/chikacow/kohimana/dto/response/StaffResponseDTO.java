package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StaffResponseDTO {
    private String username;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private WorkingStatus workingStatus;

    private StaffTeam staffTeam;
}
