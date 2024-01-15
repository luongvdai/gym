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
@Table(name = "notifications")
public class Notification extends CommonEntity {

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "receiver_id")
    private String receiverId;

    @Column(name = "sender_id")
    private String senderId;

    @ManyToOne
    @JoinColumn(name = "receiver_id", insertable = false, updatable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

}
