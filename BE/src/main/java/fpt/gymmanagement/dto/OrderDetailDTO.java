package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import fpt.gymmanagement.enums.EUserCourseSlot;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    String id;
    Double price;
    @NotNull(message = "Id đăng kí khóa học không được bỏ trống!!")
    String ptCourseId;
    @NotNull(message = "Id gói đăng kí khóa học không được bỏ trống!!")
    String orderId;
    @ApiModelProperty(hidden = true)
    PTCourseDTO ptCourse;
    @NotNull(message = "Slot khóa học không được bỏ trống!!")
    EUserCourseSlot slot;
}
