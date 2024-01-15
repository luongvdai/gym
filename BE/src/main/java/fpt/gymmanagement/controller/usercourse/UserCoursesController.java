package fpt.gymmanagement.controller.usercourse;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.request.usercourse.CourseUserSearch;
import fpt.gymmanagement.dto.request.usercourse.UserCourseStatusRequest;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.UserCourseSearch;
import fpt.gymmanagement.processor.UserCourseProcessor;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user-courses")
@Api(tags = SwaggerConfiguration.USER_COURSE_TAG)
public class UserCoursesController {
    private final UserCourseProcessor processor;

    public UserCoursesController(UserCourseProcessor processor) {
        this.processor = processor;
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute UserCourseSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

    //Những api dành cho mentor
    @GetMapping("by-mentor")
    public ResponseEntity<?> getByMentor(@ModelAttribute CourseUserSearch search) {
        return ResponseEntity.ok(processor.getCourseOfUser(search));
    }

    @GetMapping("get-mentor-by-user")
    public ResponseEntity<?> getMentorsByUserId(@ModelAttribute MentorByUserSearch search) {
        return ResponseEntity.ok(processor.getMentorByUser(search));
    }

    @GetMapping("get-course-by-user")
    public ResponseEntity<?> getCourseByUser(@ModelAttribute UserCourseSearch search) {
        return ResponseEntity.ok(processor.getCourseByUser(search));
    }

    @DeleteMapping(value = "by-mentor/out-course", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> updateOutCourse(@RequestBody String userCourseId) {
        processor.updateOutCourse(userCourseId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("by-mentor")
    public ResponseEntity<?> update(@Valid @RequestBody UserCourseStatusRequest request) {
        return ResponseEntity.ok(processor.update(request));
    }
}
