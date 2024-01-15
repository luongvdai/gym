package fpt.gymmanagement.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackUnlockRequest {
    @NotBlank(message = "ID đánh giá không được bỏ trống")
    String feedbackId;
    @NotNull(message = "Trạng thái không được bỏ trống")
    Boolean isEnable = false;
}
