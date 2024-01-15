package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.CourseDTO;
import fpt.gymmanagement.dto.UserCourseDTO;
import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.dto.request.usercourse.CourseUserSearch;
import fpt.gymmanagement.dto.request.usercourse.UserCourseStatusRequest;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.UserCourseSearch;
import fpt.gymmanagement.entity.Course;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.enums.EUserCourseStatus;
import fpt.gymmanagement.service.CourseService;
import fpt.gymmanagement.service.UserCourseService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.CourseTransform;
import fpt.gymmanagement.transform.UserCourseTransform;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserCourseProcessor extends CommonProcessor<UserCourseService, UserCourseTransform, UserCourse, UserCourseDTO, String> {
    private final UserService userService;

    private final CourseService courseService;

    private final CourseTransform courseTransform;

    private final UserTransform userTransform;
    private static final String BAD_REQUEST_MESSAGE = "Yêu cầu không đúng vui lòng thử lại!!";

    protected UserCourseProcessor(UserCourseService service, UserCourseTransform transform, UserService userService, CourseService courseService, CourseTransform courseTransform, UserTransform userTransform) {
        super(service, transform);
        this.userService = userService;
        this.courseService = courseService;
        this.courseTransform = courseTransform;
        this.userTransform = userTransform;
    }

    //this method call just for user after login
    public Page<UserCourseDTO> getPage(UserCourseSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setUserId(user.getId());
        var page = service.getPage(search);
        List<UserCourseDTO> content = page.getContent()
                .stream()
                .map(transform::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public Page<UserDTO> getMentorByUser(MentorByUserSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setUserId(user.getId());
        var page = service.getMentorByUser(search);
        List<UserDTO> content = page.getContent().stream().map(userTransform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public Page<CourseDTO> getCourseByUser(UserCourseSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setUserId(user.getId());
        var page = courseService.getCourseByUser(search);
        List<CourseDTO> content = page.getContent().stream().map(courseTransform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public Page<UserCourseDTO> getCourseOfUser(CourseUserSearch search) {
        if (CommonAppUtils.isMentor()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setPtId(user.getId());
        }
        var page = service.getByMentor(search);
        List<UserCourseDTO> content = page.getContent()
                .stream()
                .map(transform::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public void updateOutCourse(String userCourseId) {
        UserCourse userCourse = service.getOrElseThrow(userCourseId);
        this.checkPermission(userCourse);

        if (!userCourse.getStatus().equals(EUserCourseStatus.IN_PROGRESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, BAD_REQUEST_MESSAGE);
        }
        userCourse.setStatus(EUserCourseStatus.OUT_COURSE);
        service.saveOrUpdate(userCourse, userCourse.getId());
    }

    public UserCourseDTO update(UserCourseStatusRequest request) {
        UserCourse userCourse = service.getOrElseThrow(request.getUserCourseId());
        this.checkPermission(userCourse);
        userCourse.setStatus(request.getStatus());
        return transform.toDto(service.saveOrUpdate(userCourse, userCourse.getId()));
    }

    private void checkPermission(UserCourse userCourse) {
        if (!CommonAppUtils.isAdmin()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            if (!userCourse.getPtCourse().getPtId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không quản lí khóa học người dùng này!!");
            }
        }
    }
}
