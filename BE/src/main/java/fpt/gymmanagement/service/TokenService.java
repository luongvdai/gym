package fpt.gymmanagement.service;

import fpt.gymmanagement.entity.Token;
import fpt.gymmanagement.entity.User;

import java.util.List;

public interface TokenService {
    void saveToken(Token token);

    List<Token> findAllValidTokenByUsername(String username);

    void saveAll(List<Token> tokens);

    Token findByToken(String token);

    void revokeAllUserTokens(String username);

    void deleteByUsername(String username);

    void clearTokenExpired();
}

