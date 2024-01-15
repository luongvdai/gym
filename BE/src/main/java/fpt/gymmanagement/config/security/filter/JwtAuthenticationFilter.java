package fpt.gymmanagement.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.entity.Token;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.JWTService;
import fpt.gymmanagement.service.TokenService;
import fpt.gymmanagement.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final TokenService tokenService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        } else {
            try {
                jwt = authHeader.substring(7);
                username = jwtService.extractUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userService.getByUsername(username);
                    boolean isTokenValid = checkValidToken(tokenService.findByToken(jwt));

                    if (jwtService.isTokenValid(jwt, user) && isTokenValid) {
                        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
                        roles.add(new SimpleGrantedAuthority(user.getRole().getPosition()));
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                roles
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception ex) {
                log.error("Error logging in: {}", ex.getMessage());
                Map<String, String> error = new HashMap<>();
                error.put("message", switchResponseByTypeException(ex, response));
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkValidToken(Token token) {
        return Boolean.FALSE.equals(token.getExpired()) && Boolean.FALSE.equals(token.getRevoked());
    }

    private String switchResponseByTypeException(Exception ex, HttpServletResponse response) {
        if (ex instanceof SignatureException) {
            response.setStatus(BAD_REQUEST.value());
            return "Key signature not exactly";
        }
        if (ex instanceof ExpiredJwtException) {
            response.setStatus(UNAUTHORIZED.value());
            return "Token has expired";
        }
        response.setStatus(FORBIDDEN.value());
        return "Invalid authentication request!";
    }
}
