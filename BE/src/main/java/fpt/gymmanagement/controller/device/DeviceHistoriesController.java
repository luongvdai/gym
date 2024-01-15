package fpt.gymmanagement.controller.device;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.DeviceUpdateHistoryDTO;
import fpt.gymmanagement.dto.search.DeviceUpdateHistorySearch;
import fpt.gymmanagement.processor.DeviceUpdateHistoriesProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device-update-histories")
public class DeviceHistoriesController extends CommonCUDController<DeviceUpdateHistoriesProcessor, DeviceUpdateHistoryDTO, String> {
    protected DeviceHistoriesController(DeviceUpdateHistoriesProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute DeviceUpdateHistorySearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }
}
