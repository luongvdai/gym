package fpt.gymmanagement.dto.request.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterAccountRequest {

    @NotBlank(message = "Tên đăng nhập không được bỏ trống")
    String username;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    String password;

    @NotBlank(message = "Họ và tên không được bỏ trống")
    String fullName;

    @NotBlank(message = "Email không được bỏ trống")
    @Email
    String email;

    @NotBlank(message = "Số điện thoại không được bỏ trống")
    String phoneNumber;

    @NotBlank(message = "Địa chỉ không được bỏ trống")
    String address;

    @NotNull(message = "Yêu cầu nhập giới tính")
    Boolean gender;

    String branchId;

    Long dateOfBirth;
}
