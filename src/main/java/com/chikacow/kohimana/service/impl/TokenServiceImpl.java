package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.repository.TokenRepository;
import com.chikacow.kohimana.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public int save(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if (optional.isEmpty()) {
            tokenRepository.save(token);

            return token.getId();
        } else {
            Token currentToken = optional.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(currentToken);

            return currentToken.getId();
        }

    }


    @Override
    public String delete(Token tokenRecord) {

        tokenRepository.delete(tokenRecord);

        return "deleted, uncheck exception in delete though";
    }

    @Override
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("token not found, maybe not exitst, just maybe babe"));
    }
}
