package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import fpt.gymmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO extends CommonDTO {
    private String id;
    private String content;
    private String senderId;
    private String receiverId;
    private User userSenderId;
    private User userReceiverId;
}
