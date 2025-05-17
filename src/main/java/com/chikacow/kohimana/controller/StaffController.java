package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.StaffRequestDTO;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.dto.response.StaffResponseDTO;
import com.chikacow.kohimana.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "STAFF-CONTROLLER")
@RequestMapping("/api/v1/staff")
@Validated
public class StaffController {
    private final StaffService staffService;

    /**
     * Have to send the request to ADMIN first to create an user with role STAFF
     * @param staffRequestDTO
     * @return
     */

    @PostMapping
    public ResponseData<?> confirmStaff(@Valid @RequestBody StaffRequestDTO staffRequestDTO) {

        var res = staffService.confirmStaff(staffRequestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }


    @PutMapping("/{id}")
    public ResponseData<?> updateStaff(@PathVariable("id") Long staffId, @Valid @RequestBody StaffRequestDTO staffRequestDTO) {
        var res = staffService.updateStaff(staffId, staffRequestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteStaff(@PathVariable Long id) {
        var res =staffService.deleteStaff(id);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<?> getStaff(@PathVariable Long id) {

        var res = staffService.getStaffById(id);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

}
