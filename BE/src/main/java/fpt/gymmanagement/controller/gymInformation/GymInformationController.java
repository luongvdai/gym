package fpt.gymmanagement.controller.gymInformation;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.dto.search.GymInformationSearch;
import fpt.gymmanagement.processor.GymInformationProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gym-information")
public class GymInformationController extends CommonCUDController<GymInformationProcessor, GymInformationDTO, String>  {
    protected GymInformationController(GymInformationProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPageAllUserGymInformation(@ModelAttribute GymInformationSearch search) {
        return ResponseEntity.ok(processor.getPageAllUserGymInformation(search));
    }


}
