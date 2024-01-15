package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import fpt.gymmanagement.enums.EUserCourseSlot;
import fpt.gymmanagement.enums.EUserCourseStatus;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_courses")
public class UserCourse extends CommonEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "pt_course_id")
    private String ptCourseId;

    @Column(name = "order_detail_id")
    private String orderDetailId;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EUserCourseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot")
    private EUserCourseSlot slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_course_id", insertable = false, updatable = false)
    private PTCourse ptCourse;

    @OneToMany(mappedBy = "userCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<GymInformation> gymInformations;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", insertable = false, updatable = false)
    private OrderDetail orderDetail;
}
