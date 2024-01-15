package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.DeviceUpdateHistoryDTO;
import fpt.gymmanagement.dto.search.DeviceUpdateHistorySearch;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.entity.Device;
import fpt.gymmanagement.entity.DeviceUpdateHistory;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.BranchService;
import fpt.gymmanagement.service.DeviceService;
import fpt.gymmanagement.service.DeviceUpdateHistoriesService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.DeviceHistoryTransform;
import fpt.gymmanagement.transform.DeviceTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DeviceUpdateHistoriesProcessor extends CommonProcessor<DeviceUpdateHistoriesService, DeviceHistoryTransform, DeviceUpdateHistory, DeviceUpdateHistoryDTO, String> {
    private final DeviceService deviceService;
    private final DeviceTransform deviceTransform;
    private final UserService userService;
    private final BranchService branchService;

    protected DeviceUpdateHistoriesProcessor(DeviceUpdateHistoriesService service,
                                             DeviceHistoryTransform transform, DeviceService deviceService,
                                             DeviceTransform deviceTransform, UserService userService, BranchService branchService) {
        super(service, transform);
        this.deviceService = deviceService;
        this.deviceTransform = deviceTransform;
        this.userService = userService;
        this.branchService = branchService;
    }

    public Page<DeviceUpdateHistoryDTO> getPage(DeviceUpdateHistorySearch search) {
        var page = service.getPage(search);
        List<DeviceUpdateHistoryDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }


    @Override
    public DeviceUpdateHistoryDTO saveOrUpdate(DeviceUpdateHistoryDTO deviceDTO, String id) {

        Device device = deviceService.getOrElseThrow(deviceDTO.getDeviceId()); // check device exits
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount()); // check user logging
        if (id != null) {
            DeviceUpdateHistory deviceUpdateHistory = service.getOrElseThrow(id);
            this.checkPermission(user.getId(), deviceUpdateHistory.getDevice().getBranchId());// co quyen
            log.info("Branch: " + deviceUpdateHistory.getDevice().getBranchId());
            BeanUtils.copyProperties(deviceDTO, deviceUpdateHistory, "id", "userId", "createdDate", "updatedDate");
            return transform.toDto(service.saveOrUpdate(deviceUpdateHistory, id));
        }
        deviceDTO.setDevice(deviceTransform.toDto(device));
        deviceDTO.setUserId(user.getId());
        return super.saveOrUpdate(deviceDTO, null);
    }

    private void checkPermission(String username, String branchId) {
        Branch branch = branchService.getOrElseThrow(branchId);
        User user = userService.getByUsername(username);
        if (CommonAppUtils.isAdmin()) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ban khong co quyen thuc thi voi history device update cho chi nhanh " + branch.getName());
    }

    @Override
    public void deleteById(String s) {
        DeviceUpdateHistory deviceUpdateHistory = service.getOrElseThrow(s);
        this.checkPermission(CommonAppUtils.getCurrentAccount(), deviceUpdateHistory.getDevice().getBranchId());
        super.deleteById(s);
    }
}
