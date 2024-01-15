package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import fpt.gymmanagement.entity.Role;
import fpt.gymmanagement.enums.EUserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
//@Data
//@EqualsAndHashCode(callSuper = true)
public class UserDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private Long roleId;
    @ApiModelProperty(hidden = true)
    private Role role;
    @Column(columnDefinition = "text")
    private String avatar;
    private Boolean gender;
    private Long dateOfBirth;
    private Boolean isActive = true;
    private Boolean isVerify = false;
    @Enumerated(EnumType.STRING)
    private EUserType type;
    private String branchId;

    @ApiModelProperty(hidden = true)
    private BranchDTO branch;
    private String description;
}
