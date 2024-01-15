package fpt.gymmanagement.processor;

import fpt.gymmanagement.dto.CourseDTO;
import fpt.gymmanagement.dto.PTCourseDTO;
import fpt.gymmanagement.dto.UserCourseDTO;
import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.dto.request.ptcourse.RemovePTRequest;
import fpt.gymmanagement.dto.response.PersonalTrainerResponse;
import fpt.gymmanagement.dto.search.CoursesOfPTSearch;
import fpt.gymmanagement.dto.search.GymMemberSearch;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.PTInCourseSearch;
import fpt.gymmanagement.entity.Course;
import fpt.gymmanagement.entity.PTCourse;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.service.CourseService;
import fpt.gymmanagement.service.FeedbackService;
import fpt.gymmanagement.service.PTCourseService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.CourseTransform;
import fpt.gymmanagement.transform.PTCourseTransform;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PTCourseProcessor {
    private final UserService userService;
    private final CourseService courseService;
    private final PTCourseService ptCourseService;
    private final FeedbackService feedbackService;
    private final PTCourseTransform transform;
    private final UserTransform userTransform;
    private final CourseTransform courseTransform;

    public PTCourseProcessor(UserService userService, CourseService courseService,
                             PTCourseService ptCourseService, FeedbackService feedbackService, PTCourseTransform transform,
                             UserTransform userTransform, CourseTransform courseTransform) {
        this.userService = userService;
        this.courseService = courseService;
        this.ptCourseService = ptCourseService;
        this.feedbackService = feedbackService;
        this.transform = transform;
        this.userTransform = userTransform;
        this.courseTransform = courseTransform;
    }

    //API PUBLIC
    public Page<PersonalTrainerResponse> getPTByCourse(PTInCourseSearch search) {
        if (search.getIsActive() == null) {
            search.setIsActive(true);
        }
        var page = ptCourseService.getPTsByCourseId(search);
        List<PersonalTrainerResponse> content = page.getContent().stream().map(user -> {
            PersonalTrainerResponse response = PersonalTrainerResponse.getResponse(user);
            response.setStatsFeedback(feedbackService.statsStarByPT(user.getId()));
            return response;
        }).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    //API PUBLIC
    public PTCourseDTO getByPtIdAndCourseId(String ptId, String courseId) {
        PTCourse ptCourse = ptCourseService.getByPTIdAndCourseId(ptId, courseId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy gói khóa học!!"));
        return transform.toDto(ptCourse);
    }

    //API PUBLIC
    public Page<CourseDTO> getCoursesByPT(CoursesOfPTSearch search, boolean isMentor) {
        if (isMentor) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setPtId(user.getId());
        }
        var page = ptCourseService.getCoursesByPT(search);
        List<CourseDTO> content = page.getContent().stream().map(courseTransform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public Page<UserDTO> getMembersOfPT(GymMemberSearch search) {
        if (CommonAppUtils.isMentor()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setPtId(user.getId());
        }
        var page = ptCourseService.getMemberOfPT(search);
        List<UserDTO> content = page.getContent().stream().map(userTransform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public PTCourseDTO createOrUpdate(PTCourseDTO dto, String id) {
        User pt = userService.getOrElseThrow(dto.getPtId());//check pt exist
        Course course = courseService.getOrElseThrow(dto.getCourseId());//check course exist
        this.checkPermission(course.getBranchId());
        this.checkConstraintPT(pt, course);
        Optional<PTCourse> ptCourse = ptCourseService.getByPTIdAndCourseId(pt.getId(), course.getId());
        if (ptCourse.isPresent()) {
            PTCourse entity = ptCourse.get();
            entity.setIsActive(true);
            return transform.toDto(ptCourseService.saveEntity(entity));
        } else {
            return transform.toDto(ptCourseService.createOrUpdate(dto, null));
        }
    }

    public void removePTByCourseID(RemovePTRequest request) {
        User user = userService.getOrElseThrow(request.getPtId());//check pt exist
        Course course = courseService.getOrElseThrow(request.getCourseId());//check course exist
        this.checkPermission(course.getBranchId());
        ptCourseService.removePTByCourseId(user.getId(), course.getId());
    }

    public PTCourseDTO active(String id) {
        PTCourse ptCourse = ptCourseService.getOrElseThrow(id);
        this.checkPermission(ptCourse.getCourse().getBranchId());
        return transform.toDto(ptCourseService.active(id));
    }

    private void checkPermission(String branchId) {
        if (CommonAppUtils.isAdmin()) return;
        User currentUser = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        if (!(currentUser.getBranchId().equals(branchId)
                && currentUser.getRole().getPosition().equals("MANAGER"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền truy cập!!");
        }
    }

    private void checkConstraintPT(User pt, Course course) {
        if (!pt.getRole().getPosition().equals("MENTOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User truyền vào không phải là mentor!!");
        }
        if (!pt.getBranchId().equals(course.getBranchId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, pt.getFullName() +
                    " không dạy khóa học " + course.getTitle() + " ở chi nhánh khác!!");
        }
    }
}
