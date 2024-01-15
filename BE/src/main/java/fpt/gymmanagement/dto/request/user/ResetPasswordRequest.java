package fpt.gymmanagement.dto.request.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ResetPasswordRequest {
    @NotBlank(message = "Username không được bỏ trống")
    private String username;
    @NotBlank(message = "Mã reset mật khẩu không được bỏ trống")
    private String code;
    @NotBlank(message = "Mật khẩu mới không được bỏ trống")
    private String newPassword;
}
