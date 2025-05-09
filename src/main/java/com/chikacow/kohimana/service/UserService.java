package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.AccountStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    public UserDetailsService getUserDetailsService();

    public User getUserByEmail(String email);

    public User getByUsername(String userName);

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    public User updateUser(User user);


    //________UserController
    public UserResponseDTO getUserInfo(String username);

    public UserResponseDTO updateUserInfo(String username, UpdateUserRequestDTO requestDTO);

    public AccountStatus changeAccountStatus(String username);







}
