package fpt.gymmanagement.config.security.oauth2.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUser {
    private String email;
    @JsonProperty(value = "email_verified")
    private Boolean emailVerified;
    private String name;
    private String picture;
    private String sub;
}
