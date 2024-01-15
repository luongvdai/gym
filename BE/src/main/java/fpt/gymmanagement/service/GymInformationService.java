package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.dto.search.GymInformationSearch;
import fpt.gymmanagement.entity.GymInformation;
import org.springframework.data.domain.Page;

public interface GymInformationService extends CommonService<GymInformation, String> {
    Page<GymInformation> getPage(GymInformationSearch search);
    Page<GymInformation> getPagePtGymInformation(GymInformationSearch search);
    GymInformation saveOrUpdate(GymInformationDTO dto, String id);
}
