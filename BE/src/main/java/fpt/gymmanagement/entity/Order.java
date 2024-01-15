package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import fpt.gymmanagement.enums.EOrderStatus;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends CommonEntity {

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EOrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OrderDetail> orderDetails;

    @Column(name = "approve_id")
    private String approveId;

    @ManyToOne
    @JoinColumn(name = "approve_id", insertable = false, updatable = false)
    private User approvedBy;

    @Column(name = "assign_id")
    private String assignId;

    @ManyToOne
    @JoinColumn(name = "assign_id", insertable = false, updatable = false)
    private User assignedPerson;

    @Column(name = "search_order")
    private String searchOrder;
}
