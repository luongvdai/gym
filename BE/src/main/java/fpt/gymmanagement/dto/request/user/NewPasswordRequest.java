package fpt.gymmanagement.dto.request.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class NewPasswordRequest {
    @NotBlank(message = "Username không được bỏ trống")
    private String username;
    @NotBlank(message = "Email không được bỏ trống")
    private String email;
}
