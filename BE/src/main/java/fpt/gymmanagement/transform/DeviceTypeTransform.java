package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.DeviceTypeDTO;
import fpt.gymmanagement.entity.DeviceType;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceTypeTransform extends CommonTransform<DeviceType, DeviceTypeDTO> {
}
