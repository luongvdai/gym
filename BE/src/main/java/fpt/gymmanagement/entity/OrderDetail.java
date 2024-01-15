package fpt.gymmanagement.entity;

import com.restfb.types.UserProfile;
import fpt.gymmanagement.common.CommonEntity;
import fpt.gymmanagement.enums.EUserCourseSlot;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends CommonEntity {

    private Double price;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "pt_course_id")
    private String ptCourseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot")
    private EUserCourseSlot slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_course_id", insertable = false, updatable = false)
    private PTCourse ptCourse;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserCourse userCourse;
}
