package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.DeviceUpdateHistoryDTO;
import fpt.gymmanagement.entity.DeviceUpdateHistory;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceHistoryTransform extends CommonTransform<DeviceUpdateHistory, DeviceUpdateHistoryDTO> {
}
