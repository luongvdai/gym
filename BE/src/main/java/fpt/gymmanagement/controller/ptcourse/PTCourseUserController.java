package fpt.gymmanagement.controller.ptcourse;

import fpt.gymmanagement.dto.search.CoursesOfPTSearch;
import fpt.gymmanagement.dto.search.PTInCourseSearch;
import fpt.gymmanagement.processor.PTCourseProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pt-courses-user")
public class PTCourseUserController {
    private final PTCourseProcessor processor;

    public PTCourseUserController(PTCourseProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("mentor")
    public ResponseEntity<?> getMentors(@ModelAttribute PTInCourseSearch search) {
        return ResponseEntity.ok(processor.getPTByCourse(search));
    }

    @GetMapping("course")
    public ResponseEntity<?> getCourses(@ModelAttribute CoursesOfPTSearch search) {
        return ResponseEntity.ok(processor.getCoursesByPT(search, false));
    }
}
