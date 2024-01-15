package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.entity.DeviceType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceTypeService extends CommonService<DeviceType, String> {
    List<DeviceType> getAll();

    Page<DeviceType> getPage(DeviceSearch device);

}
