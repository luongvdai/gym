package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.OrderDTO;
import fpt.gymmanagement.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderTransform extends CommonTransform<Order, OrderDTO> {
}
