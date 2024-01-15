package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.DeviceUpdateHistorySearch;
import fpt.gymmanagement.entity.DeviceUpdateHistory;
import org.springframework.data.domain.Page;

public interface DeviceUpdateHistoriesService extends CommonService<DeviceUpdateHistory, String> {

    Page<DeviceUpdateHistory> getPage(DeviceUpdateHistorySearch search);
}
