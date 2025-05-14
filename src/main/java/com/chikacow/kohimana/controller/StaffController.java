package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.StaffRequestDTO;
import com.chikacow.kohimana.dto.response.StaffResponseDTO;
import com.chikacow.kohimana.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/staff")
public class StaffController {
    private final StaffService staffService;

    /**
     * Have to send the request to ADMIN first to create an user with role STAFF
     * @param staffRequestDTO
     * @return
     */

    @PostMapping
    public ResponseEntity<StaffResponseDTO> confirmStaff(@RequestBody StaffRequestDTO staffRequestDTO) {

        return ResponseEntity.ok(staffService.confirmStaff(staffRequestDTO));
    }


    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable("id") Long staffId,@RequestBody StaffRequestDTO staffRequestDTO) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, staffRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.deleteStaff(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getStaff(@PathVariable Long id) {

        return ResponseEntity.ok(staffService.getStaffById(id));
    }




}
