package fpt.gymmanagement.controller.gymInformation;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.dto.search.GymInformationSearch;
import fpt.gymmanagement.processor.GymInformationProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gym-user-information")
public class GymUserInformationController extends CommonCUDController<GymInformationProcessor, GymInformationDTO, String> {
    protected GymUserInformationController(GymInformationProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute GymInformationSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }
}
