package fpt.gymmanagement.dto.request.order;

import fpt.gymmanagement.enums.EOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderRequest {
    @NotEmpty(message = "Danh sách khóa học mua không được bỏ trống")
    @NotNull(message = "Danh sách khóa học mua không được bỏ trống")
    List<OrderItem> items;
    @ApiModelProperty(hidden = true)
    private EOrderStatus status;
}
