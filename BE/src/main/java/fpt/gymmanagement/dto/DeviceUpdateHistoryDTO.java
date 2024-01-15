package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUpdateHistoryDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;
    @NotBlank(message = "Problem không được bỏ trống")
    private String problem;
    private String userId;
    private String deviceId;
    @NotNull(message = "Chi phí bảo dưỡng không được bỏ chống")
    @Range(min = 0)
    private Double maintenanceCost;
    @ApiModelProperty(hidden = true)
    private UserDTO user;
    @ApiModelProperty(hidden = true)
    private DeviceDTO device;
}
