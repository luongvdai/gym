package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.GuestConsultantDTO;
import fpt.gymmanagement.dto.search.GuestConsultantSearch;
import fpt.gymmanagement.entity.GuestConsultant;
import fpt.gymmanagement.repository.GuestConsultantRepository;
import fpt.gymmanagement.service.GuestConsultantService;
import fpt.gymmanagement.transform.GuestConsultantTransform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuestConsultantProcessor extends CommonProcessor<GuestConsultantService, GuestConsultantTransform, GuestConsultant, GuestConsultantDTO, String> {

    private final GuestConsultantRepository repository;

    protected GuestConsultantProcessor(GuestConsultantService service, GuestConsultantTransform transform, GuestConsultantRepository repository) {
        super(service, transform);
        this.repository = repository;
    }

    public Page<GuestConsultantDTO> getPage(GuestConsultantSearch search) {
        var page = service.getPage(search);
        List<GuestConsultantDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public GuestConsultantDTO updateStatus(String id, boolean newStatus) {
        GuestConsultant guestConsultant = repository.findById(id).orElse(null);

        if (guestConsultant != null) {
            guestConsultant.setStatus(newStatus);
            guestConsultant.setUpdatedDate(new Date().getTime());

            repository.save(guestConsultant);

            GuestConsultantDTO updatedDTO = convertEntityToDTO(guestConsultant);

            return updatedDTO;
        } else {
            throw new EntityNotFoundException("Không tìm thấy entity với id: " + id);
        }
    }

    private GuestConsultantDTO convertEntityToDTO(GuestConsultant guestConsultant) {
        GuestConsultantDTO guestConsultantDTO = new GuestConsultantDTO();
        guestConsultantDTO.setStatus(guestConsultant.isStatus());
        return guestConsultantDTO;
    }
}
