package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "devices")
public class Device extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @Column(columnDefinition = "text")
    private String imageUrl;

    @Column(columnDefinition = "text")
    private String imageQR; // Thêm trường này để lưu trữ mã QR code

    private Integer rangeMaintain;

    private Boolean status;

    @Column(name = "user_id")
    private String userId;

    private Double price;

    @Column(name = "type_id")
    private String typeId;

    @ManyToOne
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    private DeviceType type;

    @Column(name = "branch_id")
        private String branchId;

    @ManyToOne
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
