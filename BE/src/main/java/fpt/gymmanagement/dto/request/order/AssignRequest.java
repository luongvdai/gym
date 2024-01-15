package fpt.gymmanagement.dto.request.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignRequest {

    @NotBlank(message = "Id của người được phân công không được bỏ trống!!")
    String assignId;

    @NotNull(message = "Id của đơn duyệt không được bỏ trống!!")
    @NotEmpty(message = "Id của đơn duyệt không được bỏ trống!!")
    List<String> orderIds;
}
