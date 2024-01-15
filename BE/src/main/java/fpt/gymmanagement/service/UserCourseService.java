package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.request.usercourse.CourseUserSearch;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.UserCourseSearch;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.entity.UserCourse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserCourseService extends CommonService<UserCourse, String> {
    void saveByList(List<UserCourse> userCourses);

    Page<UserCourse> getPage(UserCourseSearch search);

    Page<UserCourse> getByMentor(CourseUserSearch search);

    List<UserCourse> findAllInProgress();

    List<UserCourse> findAllInProgressByUserId(String userId);

    Page<User> getMentorByUser(MentorByUserSearch search);

}
