package fpt.gymmanagement.dto.request.ptcourse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemovePTRequest {

    @NotBlank(message = "ID của mentor không được bỏ trống")
    String ptId;

    @NotBlank(message = "ID của course không được bỏ trống")
    String courseId;
}
