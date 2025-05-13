package com.chikacow.kohimana.controller.admin;


import com.chikacow.kohimana.dto.response.PageResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/user")
@Slf4j
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    /**
     * In ver1, this can be treated as delete user
     * @param username username of user
     * @return account status after applied change
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PatchMapping("/{username}/change-status")
    public ResponseEntity<String> changeUserAccountStatus(@PathVariable("username") String username) {
        AccountStatus newStatus = userService.changeAccountStatus(username);

        return ResponseEntity.ok(newStatus.getDescription());
    }

    /**
     * Will be implemented in paging process
     *
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "Get list of users per pageNo", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                             @Min(5) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                             @RequestParam String sortBy) {


        PageResponse<?> users = userService.getAllUsers(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(users);

    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ResponseEntity<?> getAllUsersWithSortByMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                @RequestParam(required = false) String... sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return ResponseEntity.ok(userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
    @GetMapping("/list-with-sort-by-multiple-columns-search")
    public ResponseEntity<?> getAllUsersWithSortByMultipleColumnsAndSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                  @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                  @RequestParam(required = false) String sortBy,
                                                                  @RequestParam(required = false) String search) {
        log.info("Request get all of users with sort by multiple columns");
        return ResponseEntity.ok(userService.getAllUsersWithSortByMultipleColumnsAndSearch(pageNo, pageSize, sortBy, search));
    }


}
