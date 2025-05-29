package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.util.enums.RoleEnum;
import jakarta.servlet.http.HttpServletRequest;

import javax.management.relation.RoleNotFoundException;

public interface AuthenticationService {
    public TokenResponse authenticate(SignInRequest request);

    public TokenResponse refresh(HttpServletRequest request);

    public String removeToken(HttpServletRequest request);

    public String forgotPassword(String email);

    public String resetPassword(String secretKey);

    public String changePassword(ResetPasswordDTO resetPasswordDTO);

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) throws RoleNotFoundException;

    public UserResponseDTO registerWorker(UserRequestDTO userRequestDTO) throws RoleNotFoundException;

    public boolean isUsernameExist(String username);

    public boolean checkCurrentUserAuthorization(String role);

}
