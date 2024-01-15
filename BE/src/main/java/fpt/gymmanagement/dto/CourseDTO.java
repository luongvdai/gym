package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CourseDTO extends CommonDTO {

    @ApiModelProperty(hidden = true)
    private String id;

    @NotBlank(message = "Tên khóa học không được bỏ trống")
    private String title;

    @NotNull(message = "Thời gian khóa học không được bỏ trống")
    @Range(min = 1, max = 100, message = "Không nằm trong khoảng thời gian range process")
    private Integer rangeProcess;

    private String imageUrl;

    @NotBlank(message = "Mô tả khóa học không được bỏ trống")
    private String description;

    @NotNull(message = "Trạng thái khóa học không được bỏ trống")
    private Boolean isActive;

    @NotNull(message = "Gía khóa học không được bỏ trống")
    private Double price;

    @NotNull(message = "Id kiểu khóa học không được bỏ trống")
    private String courseTypeId;

    @NotBlank(message = "Id chi nhánh không được bỏ trống")
    private String branchId;

    private String banner;

    @ApiModelProperty(hidden = true)
    private CourseTypeDTO type;

    @ApiModelProperty(hidden = true)
    private BranchDTO branch;
}
