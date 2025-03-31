package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

}
