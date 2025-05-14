package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.StaffRequestDTO;
import com.chikacow.kohimana.dto.response.StaffResponseDTO;

public interface StaffService {

    public StaffResponseDTO confirmStaff(StaffRequestDTO staffRequestDTO);

    public StaffResponseDTO updateStaff(Long staffId, StaffRequestDTO staffRequestDTO);

    public String deleteStaff(Long id);

    public StaffResponseDTO getStaffById(Long id);

    public StaffResponseDTO getStaffByUsername(String username);
}
