package fpt.gymmanagement.controller.ptcourse;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.PTCourseDTO;
import fpt.gymmanagement.dto.request.ptcourse.RemovePTRequest;
import fpt.gymmanagement.dto.search.CoursesOfPTSearch;
import fpt.gymmanagement.dto.search.GymMemberSearch;
import fpt.gymmanagement.dto.search.PTInCourseSearch;
import fpt.gymmanagement.processor.PTCourseProcessor;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("pt-courses")
@Api(tags = SwaggerConfiguration.PT_COURSE_TAG)
public class PTCourseController {
    private final PTCourseProcessor processor;

    public PTCourseController(PTCourseProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("info")
    public ResponseEntity<?> getById(@RequestParam String ptId, @RequestParam String courseId) {
        return ResponseEntity.ok(processor.getByPtIdAndCourseId(ptId, courseId));
    }

    @GetMapping("mentor")
    public ResponseEntity<?> getMentors(@ModelAttribute PTInCourseSearch search) {
        return ResponseEntity.ok(processor.getPTByCourse(search));
    }

    @GetMapping("course")
    public ResponseEntity<?> getCourses(@ModelAttribute CoursesOfPTSearch search) {
        return ResponseEntity.ok(processor.getCoursesByPT(search, false));
    }

    @GetMapping("my-courses")
    public ResponseEntity<?> getMyCourse(@ModelAttribute CoursesOfPTSearch search) {
        return ResponseEntity.ok(processor.getCoursesByPT(search, true));
    }

    @GetMapping("gym-members")
    public ResponseEntity<?> viewMembers(@ModelAttribute GymMemberSearch search) {
        return ResponseEntity.ok(processor.getMembersOfPT(search));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid PTCourseDTO dto) {
        return ResponseEntity.ok(processor.createOrUpdate(dto, null));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody @Valid PTCourseDTO dto, @PathVariable String id) {
        return ResponseEntity.ok(processor.createOrUpdate(dto, id));
    }

    @DeleteMapping
    public ResponseEntity<?> removePTCourse(@RequestBody @Valid RemovePTRequest request) {
        processor.removePTByCourseID(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("active/{id}")
    public ResponseEntity<?> activePTCourse(@PathVariable String id) {
        return ResponseEntity.ok(processor.active(id));
    }
}
