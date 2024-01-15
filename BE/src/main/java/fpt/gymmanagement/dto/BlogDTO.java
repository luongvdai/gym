package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class BlogDTO extends CommonDTO {
    //Hidden swagger
    @ApiModelProperty(hidden = true)
    private String id;
    @ApiModelProperty(hidden = true)
    private BlogTypeDTO type;
    @ApiModelProperty(hidden = true)
    private UserDTO user;
    @ApiModelProperty(hidden = true)
    private BranchDTO branch;
    @ApiModelProperty(hidden = true)
    private String userId;

    //validate
    @NotBlank(message = "Tiêu đề blog không được bỏ trống")
    @Length(max = 200, message = "Độ dài tiêu đề không quá 200 kí tự")
    private String title;
    @NotBlank(message = "Nội dung blog không được bỏ trống")
    private String content;
    @NotBlank(message = "ID kiểu blog không được bỏ trống")
    private String typeId;
    @NotBlank(message = "ID chi nhánh không được bỏ trống")
    private String branchId;

    private String attach_url;

}
