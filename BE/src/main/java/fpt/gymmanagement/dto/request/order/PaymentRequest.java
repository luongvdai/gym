package fpt.gymmanagement.dto.request.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PaymentRequest {
    @NotBlank(message = "Id không được bỏ trống")
    private String orderId;
    @NotBlank(message = "Bankcode không được bỏ trống")
    private String bankCode;
}
