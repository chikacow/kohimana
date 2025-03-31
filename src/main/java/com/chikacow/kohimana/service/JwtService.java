package com.chikacow.kohimana.service;

import com.chikacow.kohimana.util.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateToken(UserDetails user);

    public String generateRefreshToken(UserDetails user);


    public String extractUsername(String token, TokenType tokenType);

    public boolean isValid(String token, TokenType tokenType, UserDetails user);

    public String generateResetToken(UserDetails user);
}
