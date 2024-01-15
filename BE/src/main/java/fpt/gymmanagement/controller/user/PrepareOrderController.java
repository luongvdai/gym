package fpt.gymmanagement.controller.user;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.PrepareOrderDTO;
import fpt.gymmanagement.dto.search.PrepareOrderSearch;
import fpt.gymmanagement.processor.PrepareOrderProcessor;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("prepare-orders")
@Api(tags = SwaggerConfiguration.PREPARE_ORDER_TAG)
public class PrepareOrderController {
    private final PrepareOrderProcessor processor;

    public PrepareOrderController(PrepareOrderProcessor processor) {
        this.processor = processor;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute PrepareOrderSearch search) {
        return ResponseEntity.ok(processor.getAll(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PrepareOrderDTO dto) {
        return ResponseEntity.ok(processor.create(dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        processor.remove(id);
        return ResponseEntity.ok().build();
    }
}
