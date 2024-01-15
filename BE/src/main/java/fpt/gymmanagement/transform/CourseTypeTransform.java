package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.CourseTypeDTO;
import fpt.gymmanagement.entity.CourseType;
import org.mapstruct.Mapper;

@Mapper
public interface CourseTypeTransform extends CommonTransform<CourseType, CourseTypeDTO> {
}
