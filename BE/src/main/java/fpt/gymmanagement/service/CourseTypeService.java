package fpt.gymmanagement.service;

import fpt.gymmanagement.dto.CourseTypeDTO;
import fpt.gymmanagement.dto.search.CourseTypeSearch;
import fpt.gymmanagement.entity.CourseType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseTypeService {
    Page<CourseType> getAll(CourseTypeSearch search);

    CourseType getOrElseThrow(String id);

    CourseType create(CourseTypeDTO dto);

    CourseType update(CourseTypeDTO dto, String id);

    void delete(String id);
}
