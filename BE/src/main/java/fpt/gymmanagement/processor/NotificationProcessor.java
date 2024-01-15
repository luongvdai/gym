package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.NotificationDTO;
import fpt.gymmanagement.dto.search.NotificationSearch;
import fpt.gymmanagement.entity.Notification;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.repository.UserRepository;
import fpt.gymmanagement.service.NotificationService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.NotificationTransform;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NotificationProcessor extends CommonProcessor<NotificationService, NotificationTransform, Notification, NotificationDTO, String> {

    private final UserService userService;

    private final UserRepository userRepository;

    private final UserTransform userTransform;

    protected NotificationProcessor(NotificationService service, NotificationTransform transform, UserService userService, UserRepository userRepository, UserTransform userTransform) {
        super(service, transform);
        this.userService = userService;
        this.userRepository = userRepository;
        this.userTransform = userTransform;
    }

    @Override
    public NotificationDTO saveOrUpdate(NotificationDTO notificationDTO, String id) {

        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());

        if (id != null) {
            Notification notification = service.getOrElseThrow(id);
            notification.setIsRead(notificationDTO.getIsRead());
            return transform.toDto(service.saveOrUpdate(notification, id));
        }

        notificationDTO.setSenderId(user.getId());

        NotificationDTO dto = super.saveOrUpdate(notificationDTO, null);

        User receiver = userService.getOrElseThrow(notificationDTO.getReceiverId());

        dto.setReceiver(userTransform.toDto(receiver));

        User sender = userService.getOrElseThrow(notificationDTO.getSenderId());

        dto.setSender(userTransform.toDto(sender));

        return dto;
    }

    public Page<NotificationDTO> getListIsRead(NotificationSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setReceiverId(user.getId());
        var page = service.getPage(search);
        List<NotificationDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }
}
