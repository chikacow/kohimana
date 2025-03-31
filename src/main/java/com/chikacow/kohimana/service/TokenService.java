package com.chikacow.kohimana.service;

import com.chikacow.kohimana.model.Token;

public interface TokenService {
    public int save(Token token);

    public String delete(Token tokenRecord);

    public Token getByUsername(String username);
}
