package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "guest_consuitants")
public class GuestConsultant extends CommonEntity {

    private String name;

    private String phoneNumber;

    private String email;

    private String note;

    private boolean status;

    @Column(name = "branch_id")
    private String branchId;

    @ManyToOne
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;
}
