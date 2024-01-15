package fpt.gymmanagement.config.security.oauth2;

import fpt.gymmanagement.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class OAuth2UserDetails implements OAuth2User, UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    private User user;

    public OAuth2UserDetails(Map<String, Object> attributes, List<GrantedAuthority> authorities) {
        this.attributes = attributes;
        this.user = (User) attributes.get("user");
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(user.getId());
    }
}
