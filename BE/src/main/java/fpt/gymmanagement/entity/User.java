package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import fpt.gymmanagement.enums.EUserType;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String address;

    private Boolean isVerify = false;

    private Boolean gender = false;

    @Column(columnDefinition = "text")
    private String avatar;

    private Boolean isActive = true;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "date_of_birth")
    private Long dateOfBirth;

    @Enumerated(EnumType.STRING)
    private EUserType type;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;
}
