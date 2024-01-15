package fpt.gymmanagement.dto.request.order;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class OrderItem {
    @NotBlank(message = "ID khóa học không được bỏ trống")
    private String courseId;
    private String ptId;
    @Min(value = 1, message = "Gía trị tối thiểu là 1")
    @Max(value = 4, message = "Gía trị tối đa là 4")
    private int slot = 1;
}
