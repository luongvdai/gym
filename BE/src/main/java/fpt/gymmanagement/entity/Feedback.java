package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "feedbacks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feedback extends CommonEntity {

    @Column(name = "sender_id")
    String senderId;

    @Column(name = "usercourse_id")
    String userCourseId;

    @ManyToOne
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    User sender;

    @ManyToOne
    @JoinColumn(name = "usercourse_id", insertable = false, updatable = false)
    UserCourse userCourse;

    @Column(columnDefinition = "text")
    String content;

    @Column(name = "is_enable")
    Boolean isEnable;

    @Min(value = 1)
    @Max(value = 5)
    Double star;
}
