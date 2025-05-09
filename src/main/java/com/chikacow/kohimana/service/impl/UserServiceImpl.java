package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.SaveToDBException;
import com.chikacow.kohimana.mapper.DTOMapper;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserDetails j = null;
                try {
                    UserDetails u = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("username not found"));
                    System.out.println(u.getUsername());
                    System.out.println(u.getPassword());
                    j = u;
                } catch (UsernameNotFoundException e) {
                    System.out.println("oh no");
                }
                return j;
            }
        };
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }

    @Override
    public User getByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }



    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {


        //on next version

        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public UserResponseDTO getUserInfo(String username) {
        if (!checkAuthorization(username)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }


        User retrivedUser =  userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        UserResponseDTO res = UserResponseDTO.builder()
                .firstName(retrivedUser.getFirstName())
                .lastName(retrivedUser.getLastName())
                .email(retrivedUser.getEmail())
                .gender(retrivedUser.getGender())
                .username(retrivedUser.getUsername())
                .createdAt(retrivedUser.getCreatedAt())
                .dateOfBirth(retrivedUser.getDateOfBirth())
                .phoneNumber(retrivedUser.getPhoneNumber())
                .build();

        return res;
    }

    @Override
    public UserResponseDTO updateUserInfo(String username, UpdateUserRequestDTO requestDTO) {

        if (!checkAuthorization(username)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        if (requestDTO.getFirstName() != null) {
            user.setFirstName(requestDTO.getFirstName());
        }
        if (requestDTO.getLastName() != null) {
            user.setLastName(requestDTO.getLastName());
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getGender() != null) {
            user.setGender(requestDTO.getGender());
        }
        if (requestDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(requestDTO.getDateOfBirth());
        }
        if (requestDTO.getPhone() != null) {
            user.setPhoneNumber(requestDTO.getPhone());
        }

        System.out.println(user.toString());

        User newUser = userRepository.save(user);

        UserResponseDTO res = UserResponseDTO.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .dateOfBirth(newUser.getDateOfBirth())
                .phoneNumber(newUser.getPhoneNumber())
                .build();
        return res;
    }

    @Override
    public AccountStatus changeAccountStatus(String username) {
        User user = userRepository.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        user.setIsActive(!user.getIsActive());

        User newUser = userRepository.save(user);


        return newUser.getIsActive() ? AccountStatus.ACTIVE : AccountStatus.INACTIVE;
    }

    private boolean checkAuthorization(String username) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            return true;
        }
        return false;

    }
}
