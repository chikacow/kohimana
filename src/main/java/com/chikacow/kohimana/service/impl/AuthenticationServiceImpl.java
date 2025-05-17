package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.ResetPasswordDTO;
import com.chikacow.kohimana.dto.request.SignInRequest;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.TokenResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.DatabaseException;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.SaveToDBException;
import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.model.rbac.UserHasRole;
import com.chikacow.kohimana.repository.UserHasRoleRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.*;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.enums.TokenType;
import com.chikacow.kohimana.util.helper.Separate;
import com.chikacow.kohimana.util.helper.SmoothData;
import io.micrometer.common.util.StringUtils;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.chikacow.kohimana.util.helper.SmoothData.smooth;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final RoleService  roleService;

    private final UserHasRoleRepository userHasRoleRepository;

    public TokenResponse authenticate(SignInRequest request) {
        log.info(request.getUsername() + " " + request.getPassword());
        log.info("User coming from platform: {}", request.getPlatform());
        //core logic
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        log.info("is_authenticated = {}", authenticate.isAuthenticated());
        log.info("Authorities = {}", authenticate.getAuthorities().toString());

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Wrong username or password"));

        /// save user to SCH from /access-token aka /login -> self custom
        UserDetails userDetaill = userService.getUserDetailsService().loadUserByUsername(user.getUsername());
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetaill, null, userDetaill.getAuthorities());
        //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        newContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newContext);

        //
        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        ///save to db for future management purpose
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new InvalidDataException("Username already exists");
        }

        String firstname_smooth = SmoothData.smooth(userRequestDTO.getFirstName());
        String lastname_smooth = SmoothData.smooth(userRequestDTO.getFirstName());

        User newUser = User.builder()
                .firstName(firstname_smooth)
                .lastName(lastname_smooth)
                .email(userRequestDTO.getEmail())
                .gender(Gender.fromString(userRequestDTO.getGender()))
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhone())
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .isActive(Boolean.TRUE)
                .build();

        Optional<User> retrivedUser = Optional.of(userRepository.save(newUser));
        User rtrU = retrivedUser.get();

        if (retrivedUser.isEmpty()) {
            throw new SaveToDBException("Error saving newly created user");
        }
        Role r = roleService.getRoleByName(com.chikacow.kohimana.util.enums.Role.convertToString(com.chikacow.kohimana.util.enums.Role.CUSTOMER));

        if (r == null) {
            throw new DatabaseException("Can't find role in database, potentially initialize problem");
        }

        UserHasRole userRole = UserHasRole.builder()
                .role(r)
                .user(retrivedUser.get())
                .build();



        rtrU.addRole(userRole);

        Optional<User> uu = Optional.of(userRepository.save(retrivedUser.get()));
        log.info("new user has: {}", retrivedUser.get().getRoles().size());
        log.info("authoriteis: {}", uu.get().getAuthorities().size());


        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .firstName(retrivedUser.get().getFirstName())
                .lastName(retrivedUser.get().getLastName())
                .email(retrivedUser.get().getEmail())
                .gender(retrivedUser.get().getGender())
                .username(retrivedUser.get().getUsername())
                .createdAt(retrivedUser.get().getCreatedAt())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhone())
                .build();
        return userResponseDTO;
    }


    @Override
    //@Transactional(rollbackOn = Exception.class)
    public UserResponseDTO registerWorker(UserRequestDTO userRequestDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ADMIN")) {
            throw new HaveNoAccessToResourceException("Only admin can create staff and manager");
        }
        log.info("ADMIN is hereeeeee");
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new InvalidDataException("Username already exists");
        }

        User newUser = User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .gender(Gender.fromString(userRequestDTO.getGender()))
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhone())
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .isActive(Boolean.TRUE)
                .build();

        Optional<User> retrivedUser = Optional.of(userRepository.save(newUser));
        User rtrU = retrivedUser.get();

        if (retrivedUser.isEmpty()) {
            throw new SaveToDBException("Error saving newly created user");
        }
        List<Role> listRole = new ArrayList<>();
        for (String rl : userRequestDTO.getRoles()) {
            Role r = roleService.getRoleByName(rl);
            listRole.add(r);
        }

        for (Role r : listRole) {
            UserHasRole userRole = UserHasRole.builder()
                    .role(r)
                    .user(retrivedUser.get())
                    .build();

            rtrU.addRole(userRole);
        }


        Optional<User> uu = Optional.of(userRepository.save(retrivedUser.get()));
        log.info("new user has: {}", retrivedUser.get().getRoles().size());
        log.info("authoriteis: {}", uu.get().getAuthorities().size());


        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .firstName(retrivedUser.get().getFirstName())
                .lastName(retrivedUser.get().getLastName())
                .email(retrivedUser.get().getEmail())
                .gender(retrivedUser.get().getGender())
                .username(retrivedUser.get().getUsername())
                .createdAt(retrivedUser.get().getCreatedAt())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhone())
                .build();
        return userResponseDTO;
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



}
