package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import fpt.gymmanagement.enums.EUserCourseSlot;
import fpt.gymmanagement.enums.EUserCourseStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCourseDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;
    private String userId;
    private String ptCourseId;
    private EUserCourseStatus status;
    @ApiModelProperty(hidden = true)
    private UserDTO user;
    @ApiModelProperty(hidden = true)
    private PTCourseDTO ptCourse;
    @ApiModelProperty(hidden = true)
    private EUserCourseSlot slot;
    private Long startDate;
    private Long endDate;
}
