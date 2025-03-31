package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.AuthenticationService;
import com.chikacow.kohimana.service.JwtService;
import com.chikacow.kohimana.service.TokenService;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.TokenType;
import com.chikacow.kohimana.util.helper.Separate;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    public TokenResponse authenticate(SignInRequest request) {
        System.out.println(request.getUsername() + " " + request.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));


        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Wrong username or password"));

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        //save to db for future management purpose
        tokenService.save(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");

        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Refresh token cannot be empty");
        }


        final String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);

        Optional<User> userDetails = userRepository.findByUsername(username);

        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, userDetails.get())) {
            throw new InvalidDataException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(userDetails.get());

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .userId(userDetails.get().getId())
                .build();
    }

    @Override
    public String removeToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        if (StringUtils.isBlank(token)) {
            token = request.getHeader("x-token");
            throw new InvalidDataException("Token cannot be empty");
        } else {
            token = Separate.getRidOfFirstWord(token, "Bearer");
            System.out.println("really?=" + token);
        }

        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        System.out.println("username extracted is: " + username);
        Token currentToken = tokenService.getByUsername(username);

        tokenService.delete(currentToken);

        System.out.println("Deleted");
        return "logout successfully";
    }

    @Override
    public String forgotPassword(String email) {


        // check email exists or not
        User user = userService.getUserByEmail(email);

        if (!user.isEnabled()) {
            throw new InvalidDataException("User is not active/enabled");
        }

        // generate reset token
        String resetToken = jwtService.generateResetToken(user);

        // save to db
        tokenService.save(Token.builder().username(user.getUsername()).resetToken(resetToken).build());

        // TODO send email to user
        String confirmLink = String.format("curl --location 'http://localhost:9091/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);


        return "changed password " + resetToken;
    }

    @Override
    public String resetPassword(String secretKey) {

        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        // validate token
        var user = userService.getByUsername(username);

       if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user)) {
           throw new InvalidDataException("Unallowed access to this token");
       }

        return "Reset";
    }

    @Override
    public String changePassword(ResetPasswordDTO resetPasswordDTO) {

        if (!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }

        // get user by reset token
        User user = isValidUserByToken(resetPasswordDTO.getSecretKey());

        // update password
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));

        //TODO
        userRepository.save(user);

        return "Changed";
    }

    private User isValidUserByToken(String secretKey) {
        // validate token
        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);

        // validate user is active or not
        var user = userService.getByUsername(username);

        if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user)) {
            throw new InvalidDataException("Unallowed access to this token");
        }
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        return user;
    }
}
