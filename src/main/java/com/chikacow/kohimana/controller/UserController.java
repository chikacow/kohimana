package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Slf4j(topic = "USER-CONTROLLER")
public class UserController {
    private final UserService userService;


    /**
     * ? luc con de username thi string long long string vo tu, chuyen cai thi moi loi ra loi ?
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public ResponseData<?> getUser(@PathVariable("id") Long id) {

        UserResponseDTO res = userService.getUserInfo(id);

        return ResponseData.builder()
                .data(res)
                .message("Success")
                .status(HttpStatus.OK.value())
                .build();
    }


    @PutMapping("/{id}")
    public ResponseData<?> updateUserInfo(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO requestDTO) {

        UserResponseDTO res = userService.updateUserInfo(id, requestDTO);

        return ResponseData.builder()
                .data(res)
                .message("Success")
                .status(HttpStatus.OK.value())
                .build();
    }
}
