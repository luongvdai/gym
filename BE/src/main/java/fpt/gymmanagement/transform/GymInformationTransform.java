package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.entity.GymInformation;
import org.mapstruct.Mapper;

@Mapper
public interface GymInformationTransform extends CommonTransform<GymInformation, GymInformationDTO> {
}
