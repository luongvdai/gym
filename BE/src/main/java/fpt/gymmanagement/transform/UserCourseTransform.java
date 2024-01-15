package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.UserCourseDTO;
import fpt.gymmanagement.entity.UserCourse;
import org.mapstruct.Mapper;

@Mapper
public interface UserCourseTransform extends CommonTransform<UserCourse, UserCourseDTO> {
}
