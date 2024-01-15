package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BranchDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;
//    @NotBlank(message = "Tên chi nhánh không được bỏ trống")
    private String name;
//    @NotBlank(message = "Địa chỉ chi nhánh không được bỏ trống")
    private String address;
    private String logoUrl;
    private String backgroundUrl;
    private String description;
}