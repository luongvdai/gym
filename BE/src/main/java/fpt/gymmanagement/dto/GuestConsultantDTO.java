package fpt.gymmanagement.dto;

import fpt.gymmanagement.common.CommonDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GuestConsultantDTO extends CommonDTO {

    private String id;

    @NotBlank(message = "Tên của bạn không được bỏ trống")
    private String name;

    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phoneNumber;

    @NotBlank(message = "Email Không được bỏ trống")
    private String email;

    @NotBlank(message = "Ghi chú Không được bỏ trống")
    private String note;

    private boolean status;

    @NotBlank(message = "Chi nhánh không được bỏ trống")
    private String branchId;

    private BranchDTO branch;
}
