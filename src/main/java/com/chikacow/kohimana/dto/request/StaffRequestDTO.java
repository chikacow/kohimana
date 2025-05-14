package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StaffRequestDTO {

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private WorkingStatus workingStatus;

    private StaffTeam staffTeam;
}
