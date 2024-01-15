package fpt.gymmanagement.controller;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.request.AuthenticationRequest;
import fpt.gymmanagement.dto.request.user.RegisterAccountRequest;
import fpt.gymmanagement.dto.response.AuthenticationResponse;
import fpt.gymmanagement.service.AuthenticationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Api(tags = SwaggerConfiguration.AUTHENTICATION_TAG)
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterAccountRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(service.refreshToken(request, response));
    }

    @GetMapping("oauth2")
    public ResponseEntity<AuthenticationResponse> getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok(service.OAuth2Login(oAuth2User));
    }

    @PostMapping("by-google")
    public ResponseEntity<AuthenticationResponse> authByGoogle(@RequestHeader(AUTHORIZATION) String token) {
        return ResponseEntity.ok(service.authenticateWithGoogle(token));
    }

    @PostMapping("by-facebook")
    public ResponseEntity<AuthenticationResponse> authByFacebook(@RequestHeader(AUTHORIZATION) String token) {
        return ResponseEntity.ok(service.authenticateWithFacebook(token));
    }
}
