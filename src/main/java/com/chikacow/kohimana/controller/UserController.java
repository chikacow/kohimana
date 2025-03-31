package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.UserRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping("/create-user")
    public ResponseEntity<UserRequestDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        return ResponseEntity.ok().body(userRequestDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body("User with id " + userId + " found");
    }
}
