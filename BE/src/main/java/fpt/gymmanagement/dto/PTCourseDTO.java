package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PTCourseDTO extends CommonDTO {

    @ApiModelProperty(hidden = true)
    String id;

    @NotBlank(message = "ID của mentor không được bỏ trống")
    String ptId;

    @NotBlank(message = "ID của course không được bỏ trống")
    String courseId;

    @ApiModelProperty(hidden = true)
    UserDTO user;

    @ApiModelProperty(hidden = true)
    CourseDTO course;
}
