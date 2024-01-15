package fpt.gymmanagement.controller.course;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.CourseTypeDTO;
import fpt.gymmanagement.dto.search.CourseTypeSearch;
import fpt.gymmanagement.service.CourseTypeService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("course-types")
@Api(tags = SwaggerConfiguration.COURSE_TYPE_TAG)
public class CourseTypeController {
    private final CourseTypeService service;

    public CourseTypeController(CourseTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute CourseTypeSearch search) {
        return ResponseEntity.ok(service.getAll(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getOrElseThrow(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CourseTypeDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CourseTypeDTO dto, @PathVariable String id) {
        return ResponseEntity.ok(service.update(dto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
