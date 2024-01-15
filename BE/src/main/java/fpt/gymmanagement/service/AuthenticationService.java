package fpt.gymmanagement.service;

import fpt.gymmanagement.dto.request.AuthenticationRequest;
import fpt.gymmanagement.dto.request.user.RegisterAccountRequest;
import fpt.gymmanagement.dto.response.AuthenticationResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterAccountRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    AuthenticationResponse OAuth2Login(OAuth2User oAuth2User);

    AuthenticationResponse authenticateWithGoogle(String token);

    AuthenticationResponse authenticateWithFacebook(String token);
}

