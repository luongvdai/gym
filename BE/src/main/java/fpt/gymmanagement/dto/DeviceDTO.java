package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DeviceDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;

    @NotBlank(message = "Tên thiết bị không được bỏ trống")
    private String name;

    private String imageUrl;

    private String imageQR; // Thêm trường này để lưu trữ mã QR code

    @NotNull(message = "Khoảng thời gian để sửa thiết bị không được bỏ trống")
    private Integer rangeMaintain;

    @NotNull(message = "Trạng thái thiết bị không được bỏ trống")
    private Boolean status;

    @NotBlank(message = "Id kiểu thiết bị không được bỏ trống")
    private String typeId;

    @NotNull(message = "Giá tiền thiết bị không được bỏ trống")
    private Double price;

    @ApiModelProperty(hidden = true)
    private DeviceTypeDTO type;

    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty(hidden = true)
    private UserDTO user;

    @NotBlank(message = "Yêu cầu nhập id của chi nhánh")
    private String branchId;

    @ApiModelProperty(hidden = true)
    private BranchDTO branch;
}
