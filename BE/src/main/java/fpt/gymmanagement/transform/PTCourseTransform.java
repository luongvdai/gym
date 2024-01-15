package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.PTCourseDTO;
import fpt.gymmanagement.entity.PTCourse;
import org.mapstruct.Mapper;

@Mapper
public interface PTCourseTransform extends CommonTransform<PTCourse, PTCourseDTO> {
}
