package com.chikacow.kohimana.controller.admin;


import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    /**
     * In ver1, this can be treated as delete user
     * @param username username of user
     * @return account status after applied change
     */
    @PatchMapping("/{username}/change-status")
    public ResponseEntity<String> changeUserAccountStatus(@PathVariable("username") String username) {
        AccountStatus newStatus = userService.changeAccountStatus(username);

        return ResponseEntity.ok(newStatus.getDescription());
    }

    /**
     * Will be implemented in paging process
     *
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUserInfos() {
        return null;
    }


}
