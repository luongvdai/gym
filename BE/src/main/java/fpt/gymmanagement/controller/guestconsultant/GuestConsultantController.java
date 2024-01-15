package fpt.gymmanagement.controller.guestconsultant;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.GuestConsultantDTO;
import fpt.gymmanagement.dto.search.GuestConsultantSearch;
import fpt.gymmanagement.processor.GuestConsultantProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("guest-consultant")
public class GuestConsultantController extends CommonCUDController<GuestConsultantProcessor, GuestConsultantDTO, String> {
    protected GuestConsultantController(GuestConsultantProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute GuestConsultantSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Map<String, Boolean> statusUpdate) {
        if (statusUpdate.containsKey("status")) {
            boolean newStatus = statusUpdate.get("status");
            return ResponseEntity.ok(processor.updateStatus(id, newStatus));
        } else {
            return ResponseEntity.badRequest().body("Trường 'status' là bắt buộc để cập nhật.");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        return ResponseEntity.ok(processor.getById(id));
    }

}
