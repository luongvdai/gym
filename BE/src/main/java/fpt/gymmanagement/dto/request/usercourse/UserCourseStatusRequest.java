package fpt.gymmanagement.dto.request.usercourse;

import fpt.gymmanagement.enums.EUserCourseStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserCourseStatusRequest {
    @NotBlank(message = "Id khóa học của người dùng không được bỏ trống!!")
    private String userCourseId;
    @NotNull(message = "Trạng thái khóa học của người dùng không được bỏ trống!!")
    private EUserCourseStatus status;
}
