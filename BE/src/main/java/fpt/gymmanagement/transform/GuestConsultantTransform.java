package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.GuestConsultantDTO;
import fpt.gymmanagement.entity.GuestConsultant;
import org.mapstruct.Mapper;

@Mapper
public interface GuestConsultantTransform extends CommonTransform<GuestConsultant, GuestConsultantDTO> {
}
