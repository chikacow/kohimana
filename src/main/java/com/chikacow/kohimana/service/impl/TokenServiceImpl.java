package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.repository.TokenRepository;
import com.chikacow.kohimana.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Managing existing token in charged by the system, supports recall jwt tokens
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    /**
     * Save token to database, rewrite existing records,
     * which means a user can only bond with only 1 token at a moment
     * @param token
     * @return
     */
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


    /**
     * Recall token from the user
     * @param tokenRecord
     * @return
     */
    @Override
    public String delete(Token tokenRecord) {

        tokenRepository.delete(tokenRecord);

        return "deleted, uncheck exception in delete though";
    }

    /**
     * Get on-manaing token by its owner's username
     * @param username
     * @return
     */
    @Override
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("token not found, maybe not exitst, just maybe babe"));
    }
}
