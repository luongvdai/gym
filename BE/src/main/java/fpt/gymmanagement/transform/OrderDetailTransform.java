package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.OrderDetailDTO;
import fpt.gymmanagement.entity.OrderDetail;
import org.mapstruct.Mapper;

@Mapper
public interface OrderDetailTransform extends CommonTransform<OrderDetail, OrderDetailDTO> {
}
