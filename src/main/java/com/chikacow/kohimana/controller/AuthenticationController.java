package com.chikacow.kohimana.controller;


import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.service.AuthenticationService;
import com.chikacow.kohimana.service.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO dto = authenticationService.registerUser(userRequestDTO);

        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDTO> createWorker(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO dto = authenticationService.registerWorker(userRequestDTO);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponse> login(@RequestBody SignInRequest signInRequest) {

       TokenResponse res = authenticationService.authenticate(signInRequest);

        System.out.println("hi: " + SecurityContextHolder.getContext().getAuthentication().getName());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // Real user is authenticated
            System.out.println("Real");
        } else {
            // Anonymous or unauthenticated
            System.out.println("Not real");
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
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
