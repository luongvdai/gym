package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.entity.DeviceType;
import fpt.gymmanagement.entity.QDeviceType;
import fpt.gymmanagement.repository.DeviceTypeRepository;
import fpt.gymmanagement.service.DeviceTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class DeviceTypeServiceImpl extends CommonJPAServiceImpl<DeviceType, String, DeviceTypeRepository, QDeviceType>
        implements DeviceTypeService {
    public DeviceTypeServiceImpl(EntityManager em, DeviceTypeRepository repo) {
        super(em, QDeviceType.deviceType, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy kiểu thiết bị";
    }

    @Override
    public List<DeviceType> getAll() {
        return repo.findAll();
    }

    @Override
    public Page<DeviceType> getPage(DeviceSearch search) {
        addFilter(search);
        long count = this.query.stream().count();
        this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize());
        var data = this.query.fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);

    }

    private void addFilter(DeviceSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.name.containsIgnoreCase(keyword));
        }
        this.query.orderBy(q.createdDate.desc());
    }
}
