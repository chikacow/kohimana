package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StaffResponseDTO {
    private String username;

    private Date startDate;

    private Date endDate;

    private WorkingStatus workingStatus;

    private StaffTeam staffTeam;
}
