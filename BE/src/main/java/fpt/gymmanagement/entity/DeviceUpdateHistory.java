package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "device_update_histories")
public class DeviceUpdateHistory extends CommonEntity {

    @Column(columnDefinition = "text")
    private String problem;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "maintenance_costs")
    private Double maintenanceCost;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    private Device device;
}
