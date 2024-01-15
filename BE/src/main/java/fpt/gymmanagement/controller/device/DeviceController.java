package fpt.gymmanagement.controller.device;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.DeviceDTO;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.processor.DeviceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("devices")
public class DeviceController extends CommonCUDController<DeviceProcessor, DeviceDTO, String> {
    protected DeviceController(DeviceProcessor processor) {
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

    @GetMapping("export-to-excel")
    public void exportToExcel(@ModelAttribute DeviceSearch search, HttpServletResponse response) {
        processor.exportToExcel(search, response);
    }

    @PostMapping("/generate-qr")
    public ResponseEntity<String> generateQRCodeForDevice(@RequestParam(required = false) String deviceName,
                                                          @RequestParam(required = false) String deviceId) {
        try {
            if (StringUtils.hasText(deviceName)) {
                DeviceDTO deviceDTO = processor.getDeviceByName(deviceName);
                processor.generateAndSaveQRCodeForDevice(deviceDTO);
            } else if (StringUtils.hasText(deviceId)) {
                DeviceDTO deviceDTO = processor.getById(deviceId);
                processor.generateAndSaveQRCodeForDevice(deviceDTO);
            } else {
                return new ResponseEntity<>("Either deviceName is required", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("QR code generated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to generate QR code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
