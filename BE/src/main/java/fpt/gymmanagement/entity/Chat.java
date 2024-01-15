package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
public class Chat extends CommonEntity {

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "receive_id")
    private String receiverId;

    @ManyToOne
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User userSenderId;

    @ManyToOne
    @JoinColumn(name = "receive_id", insertable = false, updatable = false)
    private User userReceiverId;

}
