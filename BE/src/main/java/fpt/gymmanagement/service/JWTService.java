package fpt.gymmanagement.service;

import fpt.gymmanagement.entity.User;

public interface JWTService {
    String extractUsername(String token);

    String generateToken(User user);

    String generateRefreshToken(User user);

    Boolean isTokenValid(String token, User user);
}
