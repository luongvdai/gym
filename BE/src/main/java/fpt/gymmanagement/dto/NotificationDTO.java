package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO extends CommonDTO {

    private String id;

    private String content;

    private Boolean isRead = false;

    private String receiverId;

    private String senderId;

    private UserDTO receiver;

    private UserDTO sender;

}
