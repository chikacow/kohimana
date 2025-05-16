package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.exception.ExpiredTokenException;
import com.chikacow.kohimana.service.JwtService;
import com.chikacow.kohimana.util.enums.TokenType;
import com.chikacow.kohimana.util.helper.Converter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.chikacow.kohimana.util.enums.TokenType.RESET_TOKEN;

@Service
public class JwtServiceImpl implements JwtService, Serializable {

    @Value("${jwt.timeout}")
    private String jwtTTLInSeconds;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.timeout-refresh-token}")
    private String refreshTokenTTLInSeconds;

    @Value("${jwt.refreshTokenKey}")
    private String refreshTokenKey;

    @Value("${jwt.resetTokenKey}")
    private String resetTokenKey;

    @Override
    public String generateToken(UserDetails user) {
        return produceToken(new HashMap<>(), user);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return produceRefreshToken(new HashMap<>(), user);
    }

    @Override
    public String generateResetToken(UserDetails user) {
        return produceResetToken(new HashMap<>(), user);
    }

    private String produceResetToken(Map<String, Object> claims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(RESET_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, TokenType tokenType, UserDetails userDetails) {
        final String username = extractUsername(token, tokenType);

        if (isTokenExpired(token, tokenType)) {
            throw new ExpiredTokenException("Token has expired");
        }
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType);
    }



    private String produceToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims) //secret payload
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String produceRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims) //secret payload
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Converter.evaluateExpression(refreshTokenTTLInSeconds)))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    //create a key which used to sign jwt
    private Key getKey(TokenType tokenType) {
        byte[] keyBytes;

        if (tokenType.equals(TokenType.ACCESS_TOKEN)) {
            keyBytes = Decoders.BASE64.decode(secretKey);

        } else if (tokenType.equals(TokenType.REFRESH_TOKEN)) {
            keyBytes = Decoders.BASE64.decode(refreshTokenKey);

        } else if (tokenType.equals(TokenType.RESET_TOKEN)) {
            keyBytes = Decoders.BASE64.decode(resetTokenKey);
        }
        else {
            throw new IllegalArgumentException("Unsupported token type: " + tokenType);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimResolver.apply(claims);

    }

    /**
     * This automatically check for the expiration of jwt to throw ExpiredJwtException
     * @param token
     * @param tokenType
     * @return
     */
    private Claims extractAllClaims(String token, TokenType tokenType) {
    return Jwts.parserBuilder()
            .setSigningKey(getKey(tokenType))
            .build()
            .parseClaimsJws(token)
            .getBody();
}

    @Override
    public boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    private Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }
}

