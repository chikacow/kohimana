package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.StaffRequestDTO;
import com.chikacow.kohimana.dto.response.StaffResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Staff;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.repository.StaffRepository;
import com.chikacow.kohimana.repository.UserHasRoleRepository;
import com.chikacow.kohimana.service.RoleService;
import com.chikacow.kohimana.service.StaffService;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.StaffTeam;
import com.chikacow.kohimana.util.enums.WorkingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final UserHasRoleRepository userHasRoleRepository;


    @Override
    public StaffResponseDTO confirmStaff(StaffRequestDTO staffRequestDTO) {

        Role staff = roleService.getRoleByName("STAFF");

        User user = userService.getUserByUsernameAndRole(staffRequestDTO.getUsername(), staff.getId());


        Staff newStaff = Staff.builder()
                .user(user)
                .startDate(staffRequestDTO.getStartDate())
                .staffTeam(staffRequestDTO.getStaffTeam())
                .workingStatus(staffRequestDTO.getWorkingStatus())
                .build();

        Staff saved = staffRepository.save(newStaff);

        StaffResponseDTO res = StaffResponseDTO.builder()
                .username(saved.getUser().getUsername())
                .startDate(saved.getStartDate())
                .staffTeam(saved.getStaffTeam())
                .workingStatus(saved.getWorkingStatus())
                .build();

        return res;
    }

    @Override
    public StaffResponseDTO updateStaff(Long staffId, StaffRequestDTO staffRequestDTO) {

        Staff existingStaff = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staffRequestDTO.getStartDate() != null) {
            existingStaff.setStartDate(staffRequestDTO.getStartDate());
        }
        if (staffRequestDTO.getWorkingStatus() != null) {
            existingStaff.setWorkingStatus(staffRequestDTO.getWorkingStatus());
        }
        if (staffRequestDTO.getStaffTeam() != null) {
            existingStaff.setStaffTeam(staffRequestDTO.getStaffTeam());
        }


        Staff saved = staffRepository.save(existingStaff);

        return StaffResponseDTO.builder()
                .username(saved.getUser().getUsername())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .workingStatus(saved.getWorkingStatus())
                .staffTeam(saved.getStaffTeam())
                .build();
    }

    @Override
    @Transactional
    public String deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff with ID " + id + " not found"));

        User user = userService.getUserByStaffId(id);
        UserHasRole uhr = null;
        Role role = roleService.getRoleByName("STAFF");
        log.info("userid: {}", user.getId());
        log.info("roleid: {}", role.getId());


        staffRepository.delete(staff);
       uhr = userHasRoleRepository.findByUserIdAndRoleName(user.getId(), role.getId());
       userHasRoleRepository.deleteUhr(uhr.getId());






        return staff.getUser().getUsername();
    }

    @Override
    public StaffResponseDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("staff not fount"));

        return StaffResponseDTO.builder()
                .username(staff.getUser().getUsername())
                .startDate(staff.getStartDate())
                .endDate(staff.getEndDate())
                .workingStatus(staff.getWorkingStatus())
                .staffTeam(staff.getStaffTeam())
                .build();
    }

    @Override
    public StaffResponseDTO getStaffByUsername(String username) {
        Staff staff = staffRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Staff with username '" + username + "' not found"));

        return StaffResponseDTO.builder()
                .username(staff.getUser().getUsername())
                .startDate(staff.getStartDate())
                .endDate(staff.getEndDate())
                .workingStatus(staff.getWorkingStatus())
                .staffTeam(staff.getStaffTeam())
                .build();
    }
}
