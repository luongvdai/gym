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
@Table(name = "prepare_orders")
public class PrepareOrder extends CommonEntity {
    String title;

    @Column(name = "user_id")
    String userId;

    @Column(name = "pt_course_id")
    String ptCourseId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "pt_course_id", insertable = false, updatable = false)
    private PTCourse ptCourse;
}
