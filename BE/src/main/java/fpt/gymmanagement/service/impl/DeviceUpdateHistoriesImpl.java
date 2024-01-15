package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.DeviceUpdateHistorySearch;
import fpt.gymmanagement.entity.DeviceUpdateHistory;
import fpt.gymmanagement.entity.QDeviceUpdateHistory;
import fpt.gymmanagement.repository.DeviceUpdateHistoriesRepository;
import fpt.gymmanagement.service.DeviceUpdateHistoriesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service
public class DeviceUpdateHistoriesImpl extends CommonJPAServiceImpl<DeviceUpdateHistory, String, DeviceUpdateHistoriesRepository, QDeviceUpdateHistory>
        implements DeviceUpdateHistoriesService {
    public DeviceUpdateHistoriesImpl(EntityManager em, DeviceUpdateHistoriesRepository repo) {
        super(em, QDeviceUpdateHistory.deviceUpdateHistory, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy thiết bị!!!";
    }

    @Override
        public Page<DeviceUpdateHistory> getPage(DeviceUpdateHistorySearch search) {
        addFilter(search);
        long count = this.query.stream().count();
        this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize());
        var data = this.query.fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    private void addFilter(DeviceUpdateHistorySearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.problem.containsIgnoreCase(keyword));
        }

        if (StringUtils.hasText(search.getDeviceId())) {
            String keyword = search.getKeyword();
            this.query.where(q.deviceId.eq(search.getDeviceId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}
