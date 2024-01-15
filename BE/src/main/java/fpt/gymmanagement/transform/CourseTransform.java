package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.CourseDTO;
import fpt.gymmanagement.entity.Course;
import org.mapstruct.Mapper;

@Mapper
public interface CourseTransform extends CommonTransform<Course, CourseDTO> {
}
