package fpt.gymmanagement.service;

import fpt.gymmanagement.dto.PTCourseDTO;
import fpt.gymmanagement.dto.search.CoursesOfPTSearch;
import fpt.gymmanagement.dto.search.GymMemberSearch;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.PTInCourseSearch;
import fpt.gymmanagement.entity.Course;
import fpt.gymmanagement.entity.PTCourse;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.entity.UserCourse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PTCourseService {
    PTCourse createOrUpdate(PTCourseDTO dto, String id);

    Page<User> getPTsByCourseId(PTInCourseSearch search);

    Page<User> getMemberOfPT(GymMemberSearch search);

    Page<Course> getCoursesByPT(CoursesOfPTSearch search);

    void removePTByCourseId(String ptId, String courseId);

    PTCourse getOrElseThrow(String id);

    Optional<PTCourse> getByPTIdAndCourseId(String ptId, String courseId);

    PTCourse active(String id);

    PTCourse saveEntity(PTCourse ptCourse);
}
