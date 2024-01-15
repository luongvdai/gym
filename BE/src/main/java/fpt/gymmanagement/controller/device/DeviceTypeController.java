package fpt.gymmanagement.controller.device;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.DeviceTypeDTO;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.processor.DeviceTypeProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device-types")
public class DeviceTypeController extends CommonCUDController<DeviceTypeProcessor, DeviceTypeDTO, String> {
    protected DeviceTypeController(DeviceTypeProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute DeviceSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }
}
