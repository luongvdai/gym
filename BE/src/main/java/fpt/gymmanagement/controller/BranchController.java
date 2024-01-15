package fpt.gymmanagement.controller;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.BranchDTO;
import fpt.gymmanagement.dto.search.BranchSearch;
import fpt.gymmanagement.processor.BranchProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("branches")
@Slf4j
public class BranchController extends CommonCUDController<BranchProcessor, BranchDTO, String> {
    protected BranchController(BranchProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute BranchSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }
}
