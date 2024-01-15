package fpt.gymmanagement.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
public abstract class CommonCUDController<P extends CommonProcessor, DTO, ID> {
    protected final P processor;

    protected CommonCUDController(P processor) {
        this.processor = processor;
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DTO dto) {
        log.info("CREATE PARAM {}: {}", dto.getClass().getName(), dto);
        return ResponseEntity.ok(processor.saveOrUpdate(dto, null));
    }

    @SuppressWarnings("unchecked")
    @PutMapping(value = "{id}")
    public ResponseEntity<?> update(@PathVariable ID id, @Valid @RequestBody DTO dto) {
        log.info("UPDATE PARAM {}: {}", dto.getClass().getName(), dto);
        return ResponseEntity.ok(processor.saveOrUpdate(dto, id));
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping("{id}")
    public void delete(@PathVariable ID id) {
        log.info("DELETE PARAM: {}", id);
        processor.deleteById(id);
    }

}
