package fpt.gymmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DeviceTypeDTO extends CommonDTO {
    @ApiModelProperty(hidden = true)
    private String id;
    @NotBlank(message = "Tên kiểu thiết bị không được bỏ trống")
    @Length(min = 4, max = 50, message = "Yêu cầu nhập độ dài phù hợp")
    private String name;
}
