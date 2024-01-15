package fpt.gymmanagement.dto.request.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Mật khẩu hiện tại không được bỏ trống")
    private String currentPassword;
    @NotBlank(message = "Mật khẩu mới không được bỏ trống")
    private String newPassword;
    @NotBlank(message = "Mật khẩu mới lần 2 không được bỏ trống")
    private String reNewPassword;
}
