package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.entity.Device;
import org.springframework.data.domain.Page;

public interface DeviceService extends CommonService<Device, String> {
    Page<Device> getPage(DeviceSearch device);

    Device getDeviceByName(String deviceName);
}
