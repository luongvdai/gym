package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.GuestConsultantSearch;
import fpt.gymmanagement.entity.GuestConsultant;
import org.springframework.data.domain.Page;

public interface GuestConsultantService extends CommonService<GuestConsultant, String> {
    Page<GuestConsultant> getPage(GuestConsultantSearch search);

}
