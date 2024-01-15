package fpt.gymmanagement.dto.request.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderByStaffRequest extends OrderRequest {
    @NotBlank(message = "người mua khóa học khng được bỏ trống")
    String buyer;
}
