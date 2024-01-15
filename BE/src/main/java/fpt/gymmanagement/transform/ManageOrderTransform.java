package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.ManageOrderDTO;
import fpt.gymmanagement.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface ManageOrderTransform extends CommonTransform<Order, ManageOrderDTO> {
}
