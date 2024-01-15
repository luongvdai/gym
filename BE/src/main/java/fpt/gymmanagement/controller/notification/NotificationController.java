package fpt.gymmanagement.controller.notification;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.NotificationDTO;
import fpt.gymmanagement.dto.search.NotificationSearch;
import fpt.gymmanagement.processor.NotificationProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("notification")
public class NotificationController extends CommonCUDController<NotificationProcessor, NotificationDTO, String> {
    protected NotificationController(NotificationProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getIsRead(@Valid @ModelAttribute NotificationSearch search){
        return ResponseEntity.ok(processor.getListIsRead(search));
    }

}
