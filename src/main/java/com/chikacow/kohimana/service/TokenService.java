package com.chikacow.kohimana.service;

import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.model.User;

public interface TokenService {
    public Token save(Token token);

    public Token generateUserAuthenticationTokens(User user);

    public String delete(Token tokenRecord);

    public Token getByUsername(String username);
}
