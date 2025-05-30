package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.request.StaffRequestDTO;
import com.chikacow.kohimana.dto.response.OrderItemResponseDTO;
import com.chikacow.kohimana.dto.response.StaffResponseDTO;
import com.chikacow.kohimana.model.*;
import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.function.Consumer;

public class StaffMapper implements DTOMapper<Staff> {
    public static Staff fromRequestDTOToEntity(StaffRequestDTO requestDTO, User user) {
        return Staff.builder()
                .user(user)
                .startDate(requestDTO.getStartDate())
                .staffTeam(StaffTeam.fromString(requestDTO.getStaffTeam()))
                .workingStatus(WorkingStatus.fromString(requestDTO.getWorkingStatus()))
                .build();
    }


    public static StaffResponseDTO fromEntityToResponseDTO(Staff staff) {
        return StaffResponseDTO.builder()
                .username(staff.getUser().getUsername())
                .startDate(staff.getStartDate())
                .endDate(staff.getEndDate())
                .workingStatus(staff.getWorkingStatus())
                .staffTeam(staff.getStaffTeam())
                .build();




    }

    public static void updateEntityFromRequestDTO(Staff staff, StaffRequestDTO requestDTO) {

        applyIfNotNull(requestDTO.getStartDate(), staff::setStartDate);
        applyIfNotNull(WorkingStatus.fromString(requestDTO.getWorkingStatus()), staff::setWorkingStatus);
        applyIfNotNull(StaffTeam.fromString(requestDTO.getStaffTeam()), staff::setStaffTeam);


    }

    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static void normalizeRequestDTO(CategoryRequestDTO requestDTO) {

        requestDTO.setName(SmoothData.smooth(requestDTO.getName()));
    }


}
