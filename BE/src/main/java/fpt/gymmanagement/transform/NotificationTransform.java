package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.NotificationDTO;
import fpt.gymmanagement.entity.Notification;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationTransform extends CommonTransform<Notification, NotificationDTO> {
}
