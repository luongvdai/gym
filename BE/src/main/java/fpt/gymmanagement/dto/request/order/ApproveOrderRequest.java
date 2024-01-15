package fpt.gymmanagement.dto.request.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ApproveOrderRequest {

    @NotBlank(message = "ID của order không được bỏ trống")
    private String orderId;

    @NotNull(message = "Yêu cầu phê duyệt không được bỏ trống")
    private Boolean isConfirm;
}
