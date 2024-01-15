package fpt.gymmanagement.service.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import fpt.gymmanagement.config.security.oauth2.user.GoogleUser;
import fpt.gymmanagement.dto.request.AuthenticationRequest;
import fpt.gymmanagement.dto.request.EmailDetails;
import fpt.gymmanagement.dto.request.user.RegisterAccountRequest;
import fpt.gymmanagement.dto.response.AuthenticationResponse;
import fpt.gymmanagement.entity.Role;
import fpt.gymmanagement.entity.Token;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.enums.EUserType;
import fpt.gymmanagement.service.*;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JWTService jwtService;
    private final TokenService tokenService;
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final EmailService emailService;

    @Override
    public AuthenticationResponse register(RegisterAccountRequest request) {
        userService.checkPrivacyCreate(request.getUsername(), request.getEmail(), request.getPhoneNumber());
        User user = this.buildFromRequest(request);
        return toResponse(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        User user = userService.getByUsername(username);
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã hết thời hạn sử dụng!!");
        }
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, token);
        return AuthenticationResponse
                .builder().accessToken(token).refreshToken(refreshToken).build();
    }

    @Override
    public AuthenticationResponse authenticateWithGoogle(String googleToken) {
        GoogleUser googleUser = getGoogleUserByToken(googleToken);
//        Optional<User> dbUser = userService.getByUsernameAndProvider(googleUser.getSub(), EUserType.GOOGLE);
        Optional<User> dbUser = userService.getByEmail(googleUser.getEmail());
        User user;
        AuthenticationResponse response;
        if (dbUser.isPresent()) {
            user = dbUser.get();
            if (Boolean.FALSE.equals(user.getIsActive())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã hết thời hạn sử dụng!!");
            }
            revokeAllUserTokens(user);
            response = this.toResponse(user);
        } else {//case CREATE
            userService.checkPrivacyCreate(googleUser.getSub(), googleUser.getEmail(), null);
            user = new User();
            String newPassword = "G" + CommonAppUtils.randomText() + "123";
            user.setUsername(googleUser.getSub());
            user.setPhoneNumber(UUID.randomUUID().toString());
            Role roleUser = roleService.getDefault();
            user.setRoleId(roleUser.getId());
            user.setRole(roleUser);
            user.setType(EUserType.GOOGLE);
            user.setPassword(newPassword);
            //sync fullName & email with Google
            syncUserInfoByOauth(user, googleUser.getName(), googleUser.getEmail());
            response = this.toResponse(user);
            this.sendLoginPassword(user.getEmail(), user.getUsername(), newPassword, user.getType());
        }
        return response;
    }

    @Override
    public AuthenticationResponse authenticateWithFacebook(String fbToken) {
        com.restfb.types.User fbUser = getFacebookUserByToken(fbToken);
//        Optional<User> dbUser = userService.getByUsernameAndProvider(fbUser.getId(), EUserType.FACEBOOK);
        Optional<User> dbUser = userService.getByEmail(fbUser.getEmail());
        User user;
        AuthenticationResponse response;
        if (dbUser.isPresent()) {
            user = dbUser.get();
            if (Boolean.FALSE.equals(user.getIsActive())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã hết thời hạn sử dụng!!");
            }
            revokeAllUserTokens(user);
            response = this.toResponse(user);
        } else {//case CREATE
            userService.checkPrivacyCreate(fbUser.getId(), fbUser.getEmail(), null);
            user = new User();
            String newPassword = "F" + CommonAppUtils.randomText() + "123";
            user.setPhoneNumber(UUID.randomUUID().toString());
            Role roleUser = roleService.getDefault();
            user.setRoleId(roleUser.getId());
            user.setRole(roleUser);
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUsername(fbUser.getId());
            user.setType(EUserType.FACEBOOK);
            user.setPassword(newPassword);
            //sync fullName & email with Google
            syncUserInfoByOauth(user, fbUser.getName(), fbUser.getEmail());
            response = this.toResponse(user);
            this.sendLoginPassword(user.getEmail(), user.getUsername(), newPassword, user.getType());
        }
        return response;
    }

    @Async
    protected void sendLoginPassword(String receiver, String username, String password, EUserType type) {
        EmailDetails email = EmailDetails
                .builder()
                .subject("Gym Management")
                .recipient(receiver)
                .msgBody("Bạn vừa đăng nhập vào hệ thống Gym Management bằng: " + type
                        + ", Đây là thông tin đăng nhập tài khoản của bạn: \nUsername: " + username + "\nPassword: " + password + "\nEmail: " + receiver
                        + "\nHãy đổi mật khẩu nhanh nhất có thể để bảo đảm vấn đề bảo mật!!"
                        + "\nXin chân thành cảm ơn")
                .build();
        emailService.sendSimpleMail(email);
    }

    private AuthenticationResponse toResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        userService.saveOrUpdate(user, user.getId());
        Token token = Token.builder()
                .user(user)
                .username(user.getUsername())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenService.findAllValidTokenByUsername(user.getUsername());
        if (validTokens.isEmpty())
            return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validTokens);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String refreshToken = authHeader.substring(7);
            final String username = jwtService.extractUsername(refreshToken);
            if (username != null) {
                User user = userService.getByUsername(username);
                if (jwtService.isTokenValid(refreshToken, user)) {
                    String accessToken = jwtService.generateToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ!!");
    }

    @Override
    public AuthenticationResponse OAuth2Login(OAuth2User oAuth2User) {
        User user = oAuth2User.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đăng nhập OAuth2 thất bại!!");
        }
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, token);
        return AuthenticationResponse
                .builder().accessToken(token).refreshToken(refreshToken).build();
    }

    private User buildFromRequest(RegisterAccountRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        Role roleUser = roleService.getDefault();
        user.setRoleId(roleUser.getId());
        user.setRole(roleUser);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setType(EUserType.NORMAL);
        return user;
    }

    private GoogleUser getGoogleUserByToken(String token) {
        try {
            return restTemplate
                    .getForEntity("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token, GoogleUser.class)
                    .getBody();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi chứng thực tài khoản google!!");
        }
    }

    private com.restfb.types.User getFacebookUserByToken(String fbToken) {
        try {
            FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.VERSION_12_0);
            return facebookClient.fetchObject("me", com.restfb.types.User.class,
                    Parameter.with("fields", "id,name,email"));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi chứng thực tài khoản Facebook!!");
        }
    }

    private void syncUserInfoByOauth(User user, String fullName, String email) {
        user.setEmail(email);
        user.setFullName(fullName);
    }
}
