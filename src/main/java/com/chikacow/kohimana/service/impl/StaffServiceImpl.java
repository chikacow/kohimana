package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.StaffRequestDTO;
import com.chikacow.kohimana.dto.response.StaffResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.mapper.StaffMapper;
import com.chikacow.kohimana.model.Staff;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.repository.StaffRepository;
import com.chikacow.kohimana.repository.UserHasRoleRepository;
import com.chikacow.kohimana.service.StaffService;
import com.chikacow.kohimana.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j(topic = "STAFF-SERVICE")
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final UserHasRoleRepository userHasRoleRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaffResponseDTO confirmStaff(StaffRequestDTO staffRequestDTO) {

        ///annoying
        Role staff = roleService.getRoleByName("STAFF");
        User user = userService.getUserByUsernameAndRole(staffRequestDTO.getUsername(), staff.getId());

        Staff newStaff = StaffMapper.fromRequestDTOToEntity(staffRequestDTO, user);

        staffRepository.save(newStaff);

        return StaffMapper.fromEntityToResponseDTO(newStaff);
    }

    @Override
    @Transactional
    public StaffResponseDTO updateStaff(Long staffId, StaffRequestDTO staffRequestDTO) {
        Staff staff = getById(staffId);

        StaffMapper.updateEntityFromRequestDTO(staff, staffRequestDTO);

        staffRepository.save(staff);

        return StaffMapper.fromEntityToResponseDTO(staff);
    }

    ///bunch of shiet
    @Override
    @Transactional
    public String deleteStaff(Long id) {
        Staff staff = getById(id);
        staffRepository.delete(staff);

        User user = userService.getUserByStaffId(id);
        Role role = roleService.getRoleByName("STAFF");
        log.info("userid: {}", user.getId());
        log.info("roleid: {}", role.getId());
        UserHasRole uhr = userHasRoleRepository.findByUserIdAndRoleName(user.getId(), role.getId());
        userHasRoleRepository.deleteUhr(uhr.getId());

        return staff.getUser().getUsername();
    }

    @Override
    public StaffResponseDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("staff not found"));

        return StaffMapper.fromEntityToResponseDTO(staff);
    }

    @Override
    public Staff getById(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
    }

    @Override
    public StaffResponseDTO getStaffByUsername(String username) {
        Staff staff = staffRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Staff with username '" + username + "' not found"));

        return StaffMapper.fromEntityToResponseDTO(staff);
    }
}
