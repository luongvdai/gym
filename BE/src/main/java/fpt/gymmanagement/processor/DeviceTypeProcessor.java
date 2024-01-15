package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.DeviceTypeDTO;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.entity.DeviceType;
import fpt.gymmanagement.service.DeviceTypeService;
import fpt.gymmanagement.transform.DeviceTypeTransform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceTypeProcessor extends CommonProcessor<DeviceTypeService, DeviceTypeTransform, DeviceType, DeviceTypeDTO, String> {
    protected DeviceTypeProcessor(DeviceTypeService service, DeviceTypeTransform transform) {
        super(service, transform);
    }

    public List<DeviceTypeDTO> getAll() {
        return service.getAll().stream().map(transform::toDto).collect(Collectors.toList());
    }

    public Page<DeviceTypeDTO> getPage(DeviceSearch search) {
        var page = service.getPage(search);
        List<DeviceTypeDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }
}
