package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.SaveToDBException;
import com.chikacow.kohimana.mapper.UserMapper;
import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.model.redis.RedisToken;
import com.chikacow.kohimana.repository.redis.RedisTokenRepository;
import com.chikacow.kohimana.repository.UserHasRoleRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.*;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.enums.RoleEnum;
import com.chikacow.kohimana.util.enums.TokenType;
import com.chikacow.kohimana.util.helper.Separate;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final RedisTokenService redisTokenService;

    private final PasswordEncoder passwordEncoder;

    private final RoleService  roleService;

    @PersistenceContext
    private final EntityManager entityManager;

    public TokenResponse authenticate(SignInRequest request) {
        log.info(request.getUsername() + " " + request.getPassword());
        log.info("User coming from platform: {}", request.getPlatform());
        //core logic
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        log.info("is_authenticated = {}", authenticate.isAuthenticated());
        log.info("Authorities = {}", authenticate.getAuthorities().toString());

        User user = userService.getByUsername(request.getUsername());

        /// save user to SCH from /access-token aka /login -> self custom
        saveToSecurityContextHolder(user);

        Token userToken = tokenService.generateUserAuthenticationTokens(user);

        redisTokenService.save(userToken);

        return TokenResponse.builder()
                .accessToken(userToken.getAccessToken())
                .refreshToken(userToken.getRefreshToken())
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
        User userDetails = userService.getByUsername(username);

        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, userDetails)) {
            throw new InvalidDataException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(userDetails);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .userId(userDetails.getId())
                .build();
    }

    @Override
    @Transactional
    public String removeToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
       ///String token = request.getHeader("x-token");
        if (StringUtils.isBlank(token)) {
            throw new InvalidDataException("Token cannot be empty");
        }
        token = Separate.getRidOfFirstWord(token, "Bearer");
        System.out.println("really?=" + token);

        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        System.out.println("username extracted is: " + username);

        Token currentToken = tokenService.getByUsername(username);

        ///check if the redis is existed, if ttl is over, then bug will happend since this version performs both in sql and redis
        RedisToken redisToken = redisTokenService.getById(username);
        redisTokenService.delete(username);

        tokenService.delete(currentToken);

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

        ///redis
        redisTokenService.save(RedisToken.builder().id(user.getUsername()).resetToken(resetToken).build());

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

       redisTokenService.getById(username);

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
        userService.save(user);

        return "Changed";
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        isUsernameExist(userRequestDTO.getUsername());

        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        User newUser = UserMapper.fromRequestDTOToEntity(userRequestDTO, encodedPassword);

        roleService.addRoleToUser(newUser, RoleEnum.CUSTOMER);
        entityManager.persist(newUser);
        //entityManager.flush();

        log.info("new user has: {}", newUser.getRoles().size());
        log.info("authoriteis: {}", newUser.getAuthorities().size());

        return UserMapper.fromEntityToResponseDTO(newUser);
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserResponseDTO registerWorker(UserRequestDTO userRequestDTO) {
        checkCurrentUserAuthorization("ADMIN");

        isUsernameExist(userRequestDTO.getUsername());

        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        User newUser = UserMapper.fromRequestDTOToEntity(userRequestDTO, encodedPassword);

        roleService.addManyRoleToUser(newUser, userRequestDTO.getRoles());

        entityManager.persist(newUser);
        entityManager.flush();
        log.info("new user has: {}", newUser.getRoles().size());
        log.info("authoriteis: {}", newUser.getAuthorities().size());

        return UserMapper.fromEntityToResponseDTO(newUser);
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userService.isUsernameExist(username);
    }

    @Override
    public boolean checkCurrentUserAuthorization(String role) {
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(role)) {
            throw new HaveNoAccessToResourceException("Current user is not " + role );
        }
        log.info(role + " is hereeeeee");
        return true;
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

    private boolean checkValidUsername(String input) {
        return input != null && input.matches(".*\\s.*");
    }

    private void saveToSecurityContextHolder(User user) {
        UserDetails userDetail = userService.getUserDetailsService().loadUserByUsername(user.getUsername());
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        newContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newContext);

    }



}
