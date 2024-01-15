package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.PrepareOrderDTO;
import fpt.gymmanagement.entity.PrepareOrder;
import org.mapstruct.Mapper;

@Mapper
public interface PrepareOrderTransform extends CommonTransform<PrepareOrder, PrepareOrderDTO> {
}
