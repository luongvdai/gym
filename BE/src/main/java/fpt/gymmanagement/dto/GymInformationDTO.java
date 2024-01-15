package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class GymInformationDTO extends CommonDTO {

    private String id;

    @NotBlank(message = "UserId không được bỏ trống")
    private String userId;

    @NotBlank(message = "userCourseId không được bỏ trống")
    private String userCourseId;

    @NotNull(message = "Chiều cao không được bỏ trống")
    @Range(min = 0, message = "Không nằm trong khoảng chiều cao quy định")
    private Double height; // chiều cao

    @NotNull(message = "Cân nặng không được bỏ trống")
    @Range(min = 0, message = "Không nằm trong khoảng cân nặng quy định")
    private Double weight; // nặng

    @NotNull(message = "Tỉ lệ nước không được bỏ trống")
    private Double ecw_tbw; //tỷ lệ nước ngoại bào (ECW/TBW)

    private Double ecw_tbwNormalTo; //tỷ lệ nước ngoại bào (ECW/TBW)

    private Double ecw_tbwNormal; //tỷ lệ nước ngoại bào (ECW/TBW)

    @NotNull(message = "Protein không được bỏ trống")
    private Double protein;

    private Double proteinNormal;

    private Double proteinNormalTo;

    @NotNull(message = "Pbf không được bỏ trống")
    private Double pbf;

    @NotNull(message = "pbfNormal không được bỏ trống")
    private Double pbfNormal;

    private Double pbfNormalTo;

    @NotNull(message = "Whr không được bỏ trống")
    private Double whr;

    private Double whrNormal;

    private Double whrNormalTo;

    @NotNull(message = "Vòng một không được bỏ trống")
    private Double bustMeasurement;

    @NotNull(message = "Vòng hai không được bỏ trống")
    private Double waistMeasurement;

    @NotNull(message = "Vòng ba không được bỏ trống")
    private Double hipMeasurement;

    @NotBlank(message = "Note không được bỏ trống")
    private String note;

    private UserDTO user;

    private UserDTO mentor;

    private CourseDTO course;

}
