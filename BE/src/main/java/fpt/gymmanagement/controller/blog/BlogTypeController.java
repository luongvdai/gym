package fpt.gymmanagement.controller.blog;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.BlogTypeDTO;
import fpt.gymmanagement.processor.BlogTypeProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blog-types")
public class BlogTypeController extends CommonCUDController<BlogTypeProcessor, BlogTypeDTO, String> {
    protected BlogTypeController(BlogTypeProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(processor.getAll());
    }

}
