package fpt.gymmanagement.dto.request.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends RegisterAccountRequest {

    private String branchId;

    private Long roleId;

    private String avatar;

    private Boolean isVerify = false;

    private Boolean isActive = false;

    private String description;
}
