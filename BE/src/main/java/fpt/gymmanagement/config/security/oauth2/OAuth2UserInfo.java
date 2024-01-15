package fpt.gymmanagement.config.security.oauth2;

import lombok.Data;

import java.util.Map;

@Data
public class OAuth2UserInfo {
    private String email;
    private String name;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
    }

    protected Map<String, Object> attributes;
}
