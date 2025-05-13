package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    public TokenResponse authenticate(SignInRequest request);

    public TokenResponse refresh(HttpServletRequest request);

    public String removeToken(HttpServletRequest request);

    public String forgotPassword(String email);

    public String resetPassword(String secretKey);

    public String changePassword(ResetPasswordDTO resetPasswordDTO);

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    public UserResponseDTO registerWorker(UserRequestDTO userRequestDTO);
}
