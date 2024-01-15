package fpt.gymmanagement.controller.feedback;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.FeedbackDTO;
import fpt.gymmanagement.dto.request.FeedbackUnlockRequest;
import fpt.gymmanagement.dto.search.FeedbackSearch;
import fpt.gymmanagement.processor.FeedbackProcessor;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("feedbacks")
@Api(tags = SwaggerConfiguration.FEEDBACK_TAG)
public class FeedbackController {
    private final FeedbackProcessor processor;

    public FeedbackController(FeedbackProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("/by-pt")
    public ResponseEntity<?> getByPt(FeedbackSearch search) {
        return ResponseEntity.ok(processor.getByPt(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getByUserCourseId(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid FeedbackDTO dto) {
        return ResponseEntity.ok(processor.create(dto, null));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody @Valid FeedbackDTO dto, @PathVariable String id) {
        return ResponseEntity.ok(processor.update(dto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        processor.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("action")
    public ResponseEntity<?> update(@RequestBody @Valid FeedbackUnlockRequest request) {
        processor.unlockFeedback(request);
        return ResponseEntity.ok().build();
    }
}
