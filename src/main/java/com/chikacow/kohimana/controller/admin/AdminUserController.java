package com.chikacow.kohimana.controller.admin;


import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.AccountStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/user")
@Slf4j(topic = "ADMIN-USER-CONTROLLER")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    /**
     * In ver1, this can be treated as delete user
     * @param id username of user
     * @return account status after applied change
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PatchMapping("/{id}/change-status")
    public ResponseData<?> changeUserAccountStatus(@PathVariable("id") Long id) {
        AccountStatus newStatus = userService.changeAccountStatus(id);

        Map<String, String> res = new HashMap<>();
        res.put("userId", id.toString());
        res.put("status", newStatus.getDescription());


        return ResponseData.builder()
                .data(res)
                .message("Success")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Will be implemented in paging process
     *
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "Get list of users per pageNo", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                       @RequestParam(required = true) int pageSize,
                                       @RequestParam String sortBy) {


        PageResponse<?> res = userService.getAllUsers(pageNo, pageSize, sortBy);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();

    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ResponseData<?> getAllUsersWithSortByMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                @RequestParam(required = false) String... sorts) {

        PageResponse<?> res  = userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/list-with-search")
    public ResponseData<?> getAllUsersWithSortByMultipleColumnsAndSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                  @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                  @RequestParam(required = false) String search) {


        PageResponse<?> res = userService.getAllUsersWithSearch(pageNo, pageSize, search);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }


}
