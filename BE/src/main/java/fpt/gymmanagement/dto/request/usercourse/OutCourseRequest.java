package fpt.gymmanagement.dto.request.usercourse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutCourseRequest {
    private String userId;
    private String ptCourseId;
}
