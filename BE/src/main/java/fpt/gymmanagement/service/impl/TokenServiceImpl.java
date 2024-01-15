package fpt.gymmanagement.service.impl;

import fpt.gymmanagement.entity.Token;
import fpt.gymmanagement.repository.TokenRepository;
import fpt.gymmanagement.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository repository;

    @Override
    public void saveToken(Token token) {
        repository.save(token);
    }

    @Override
    public List<Token> findAllValidTokenByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        repository.saveAll(tokens);
    }

    @Override
    public Token findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token does not exist!!"));
    }

    @Override
    public void revokeAllUserTokens(String username) {
        List<Token> validTokens = this.findAllValidTokenByUsername(username);
        if (validTokens.isEmpty())
            return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        this.saveAll(validTokens);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.deleteAll(repository.getAllByUsername(username));
    }

    @Override
    public void clearTokenExpired() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long sevenDaysAgoTimestamp = calendar.getTimeInMillis();
        List<Token> tokens = repository.getLessThanTime(sevenDaysAgoTimestamp);
        if (!tokens.isEmpty()) {
            repository.deleteAll(tokens);
        }
    }
}

