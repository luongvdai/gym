package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.BlogSearch;
import fpt.gymmanagement.dto.search.NotificationSearch;
import fpt.gymmanagement.entity.Blog;
import fpt.gymmanagement.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService extends CommonService<Notification, String> {
    Page<Notification> getPage(NotificationSearch search);

}
