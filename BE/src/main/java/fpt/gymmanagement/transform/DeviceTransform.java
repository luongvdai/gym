package fpt.gymmanagement.transform;


import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.DeviceDTO;
import fpt.gymmanagement.entity.Device;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceTransform extends CommonTransform<Device, DeviceDTO> {
}
