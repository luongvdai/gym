package fpt.gymmanagement.controller;

import fpt.gymmanagement.processor.RoleProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleProcessor processor;

    public RoleController(RoleProcessor processor) {
        this.processor = processor;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(processor.getAll());
    }

    @GetMapping("filter")
    public ResponseEntity<?> getFilter() {
        return ResponseEntity.ok(processor.getFilter());
    }
}
