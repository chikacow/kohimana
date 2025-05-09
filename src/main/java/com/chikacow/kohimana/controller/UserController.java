package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;


    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        //on next version

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {

        UserResponseDTO res = userService.getUserInfo(username);


        log.info("Hi from logger");
        return ResponseEntity.ok().body(res);
    }


    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDTO> updateUserInfo(@PathVariable String username, @RequestBody UpdateUserRequestDTO requestDTO) {

        UserResponseDTO res = userService.updateUserInfo(username, requestDTO);

        return ResponseEntity.ok().body(res);
    }
}
