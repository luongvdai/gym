package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.response.CourseStatsResponse;
import fpt.gymmanagement.dto.search.CourseSearch;
import fpt.gymmanagement.dto.search.UserCourseSearch;
import fpt.gymmanagement.entity.Course;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CourseService extends CommonService<Course, String> {
    Page<Course> getPage(CourseSearch search);

    Page<Course> getCourseByUser(UserCourseSearch search);

    List<CourseStatsResponse> getTopCourse(Map<String, Integer> courseCountMap);
}
