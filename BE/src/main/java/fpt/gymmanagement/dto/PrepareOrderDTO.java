package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrepareOrderDTO extends CommonDTO {

    @ApiModelProperty(hidden = true)
    String id;

    @ApiModelProperty(value = "Tiêu đề của gói trong giỏ khóa học")
    String title;

    @ApiModelProperty(hidden = true)
    String userId;

    @NotBlank(message = "ID gói khóa học không được bỏ trống!!")
    String ptCourseId;

    @ApiModelProperty(hidden = true)
    PTCourseDTO ptCourse;
}
