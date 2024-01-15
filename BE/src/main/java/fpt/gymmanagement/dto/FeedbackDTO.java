package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackDTO extends CommonDTO {
    @NotBlank(message = "ID khóa học người dùng không được bỏ trống")
    String userCourseId;
    @NotBlank(message = "Lời đánh giá không được bỏ trống")
    String content;

    @NotNull(message = "Đánh giá sao không được bỏ trống")
    @Min(value = 1, message = "Gía trị không thể nhỏ hơn 1")
    @Max(value = 5, message = "Gía trị không thể lớn hơn 5")
    Double star;

    @ApiModelProperty(hidden = true)
    String id;
    @ApiModelProperty(hidden = true)
    UserDTO sender;
    @ApiModelProperty(hidden = true)
    UserCourseDTO userCourse;
    @ApiModelProperty(hidden = true)
    String senderId;
    @ApiModelProperty(hidden = true)
    Boolean isEnable = true;
}
