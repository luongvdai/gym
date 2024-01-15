package fpt.gymmanagement.controller.blog;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.BlogDTO;
import fpt.gymmanagement.dto.search.BlogSearch;
import fpt.gymmanagement.processor.BlogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("blogs")
@Slf4j
public class BlogController extends CommonCUDController<BlogProcessor, BlogDTO, String> {
    protected BlogController(BlogProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute BlogSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

    @GetMapping("user-by-role")
    public ResponseEntity<?> getUserManage(String branchId) {
        return ResponseEntity.ok(processor.getUserManageAdminMentor(branchId));
    }

}
