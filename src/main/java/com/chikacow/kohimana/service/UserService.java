package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.AccountStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    public UserDetailsService getUserDetailsService();

    public User getUserByEmail(String email);

    public User getByUsername(String username);

    //public UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    public User updateUser(User user);

    public PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy);

    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    public PageResponse<?> getAllUsersWithSortByMultipleColumnsAndSearch(int pageNo, int pageSize, String sortBy, String search);

    //________UserController
    public UserResponseDTO getUserInfo(String username);

    public UserResponseDTO updateUserInfo(String username, UpdateUserRequestDTO requestDTO);

    public AccountStatus changeAccountStatus(String username);









}
