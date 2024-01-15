package fpt.gymmanagement.controller.course;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.CourseDTO;
import fpt.gymmanagement.dto.search.CourseSearch;
import fpt.gymmanagement.processor.CourseProcessor;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("courses")
@Api(tags = SwaggerConfiguration.COURSE_TAG)
public class CourseController extends CommonCUDController<CourseProcessor, CourseDTO, String> {
    protected CourseController(CourseProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@Valid @ModelAttribute CourseSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("export-to-excel")
    public void exportToExcel(@Valid @ModelAttribute CourseSearch search, HttpServletResponse response) {
        processor.exportToExcel(search, response);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

}
