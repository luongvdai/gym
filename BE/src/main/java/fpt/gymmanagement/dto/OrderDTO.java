package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import fpt.gymmanagement.enums.EOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;

    private String userId;

    @ApiModelProperty(hidden = true)
    private UserDTO user;

    private EOrderStatus status;

    private List<OrderDetailDTO> orderDetails;

    @ApiModelProperty(hidden = true)
    private UserDTO approvedBy;

    @ApiModelProperty(hidden = true)
    private BranchDTO branch;
}
