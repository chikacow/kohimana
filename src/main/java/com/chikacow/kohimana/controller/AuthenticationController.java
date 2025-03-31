package com.chikacow.kohimana.controller;


import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.service.AuthenticationService;
import com.chikacow.kohimana.service.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponse> login(@RequestBody SignInRequest signInRequest) {


        return new ResponseEntity<>(authenticationService.authenticate(signInRequest), HttpStatus.OK);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refresh(request), HttpStatus.OK);
    }


    @PostMapping("/remove-token")
    public ResponseEntity<String> removeToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.removeToken(request), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email),HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String secretKey) {
        return new ResponseEntity<>(authenticationService.resetPassword(secretKey),HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return new ResponseEntity<>(authenticationService.changePassword(resetPasswordDTO),HttpStatus.OK);
    }
}
