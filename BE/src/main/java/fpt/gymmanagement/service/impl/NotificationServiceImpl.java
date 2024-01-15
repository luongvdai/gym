package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.dto.search.NotificationSearch;
import fpt.gymmanagement.entity.Notification;
import fpt.gymmanagement.entity.QNotification;
import fpt.gymmanagement.repository.NotificationRepository;
import fpt.gymmanagement.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class NotificationServiceImpl extends CommonJPAServiceImpl<Notification, String, NotificationRepository, QNotification>
implements NotificationService {
    public NotificationServiceImpl(EntityManager em, NotificationRepository repo) {
        super(em, QNotification.notification, repo);
    }

    @Override
    protected String notFoundMessage() {
        return null;
    }

    @Override
    public Page<Notification> getPage(NotificationSearch search) {
        this.addFilterNotification(search);
        long count = this.query.stream().count();
        var data = this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize()).fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    private void addFilterNotification(NotificationSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (search.getIsRead() != null) {
            this.query.where(q.isRead.eq(search.getIsRead()));
        }

        if (StringUtils.hasText(search.getReceiverId())) {
            this.query.where(q.receiverId.eq(search.getReceiverId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}
