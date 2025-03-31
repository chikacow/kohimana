package com.chikacow.kohimana.service;

import com.chikacow.kohimana.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    public UserDetailsService getUserDetailsService();

    public User getUserByEmail(String email);

    public User getByUsername(String userName);




}
