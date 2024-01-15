package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.entity.Device;
import fpt.gymmanagement.entity.QDevice;
import fpt.gymmanagement.repository.DeviceRepository;
import fpt.gymmanagement.service.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service
public class DeviceServiceImpl extends CommonJPAServiceImpl<Device, String, DeviceRepository, QDevice>
        implements DeviceService {
    public DeviceServiceImpl(EntityManager em, DeviceRepository repo) {
        super(em, QDevice.device, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy thiết bị!!!";
    }

    @Override
    public Page<Device> getPage(DeviceSearch search) {
        this.addFilterDevice(search);
        long count = this.query.stream().count();
        var data = this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize()).fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    private void addFilterDevice(DeviceSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.name.containsIgnoreCase(keyword));
        }

        if (search.getStatus() != null) {
            this.query.where(q.status.eq(search.getStatus()));
        }

        if (StringUtils.hasText(search.getTypeId())) {
            this.query.where(q.typeId.eq(search.getTypeId()));
        }

        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.branchId.eq(search.getBranchId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }

    @Override
    public Device getDeviceByName(String deviceName) {
        return repo.findByName(deviceName);
    }
}
