package fpt.gymmanagement.config.security.oauth2;

import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.enums.EUserType;
import fpt.gymmanagement.service.RoleService;
import fpt.gymmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2DetailService extends DefaultOAuth2UserService {
    private final UserService userService;
    private final RoleService roleService;

    //provider means registrationId(google, facebook)

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return checkingOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User checkingOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
        String provider = request.getClientRegistration().getRegistrationId();
        EUserType type = getSpecificType(provider);
        OAuth2UserInfo oAuth2UserDetails = new OAuth2UserInfo(oAuth2User.getAttributes());
        String username = getUsernameByIdOAuth2User(provider, oAuth2UserDetails);

        Optional<User> user = userService.getByUsernameAndProvider(username, type);
        User userDetail;
        if (user.isPresent()) {
            userDetail = user.get();
            if (!userDetail.getType().equals(type)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login wrong site!!");
            } else {
                updateOAuth2UserDetail(userDetail, provider, oAuth2UserDetails);
            }
        } else {
            userDetail = registerByOAuth2User(provider, oAuth2UserDetails);
        }
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(userDetail.getRole().getPosition()));
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("user", userDetail);
        return new OAuth2UserDetails(attributes,
                authorities);
    }

    private EUserType getSpecificType(String provider) {
        switch (provider) {
            case "facebook":
                return EUserType.FACEBOOK;
            case "google":
                return EUserType.GOOGLE;
            default:
                return EUserType.NORMAL;
        }
    }

    private String getUsernameByIdOAuth2User(String provider, OAuth2UserInfo oAuth2User) {
        switch (provider) {
            case "facebook":
                return (String) oAuth2User.getAttributes().get("id");
            case "google":
                return (String) oAuth2User.getAttributes().get("sub");
            default:
                return (String) oAuth2User.getAttributes().get("email");
        }
    }

    private User registerByOAuth2User(String provider, OAuth2UserInfo userDetails) {
        User user = new User();
        String idUsername = getUsernameByIdOAuth2User(provider, userDetails);
        user.setUsername(idUsername);
        user.setFullName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        EUserType type = provider.toUpperCase().equals(EUserType.GOOGLE.name()) ? EUserType.GOOGLE : EUserType.FACEBOOK;
        user.setType(type);
        user.setRole(roleService.getDefault());
        return userService.saveOrUpdate(user, null);
    }

    private void updateOAuth2UserDetail(User user, String provider, OAuth2UserInfo userDetails) {
        String idUsername = getUsernameByIdOAuth2User(provider, userDetails);
        user.setFullName(userDetails.getName());
        user.setUsername(idUsername);
        user.setEmail(userDetails.getEmail());
        userService.saveOrUpdate(user, user.getId());
    }
}
