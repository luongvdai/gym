package fpt.gymmanagement.dto.request.usercourse;

import fpt.gymmanagement.common.CommonSearch;
import fpt.gymmanagement.enums.EUserCourseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseUserSearch extends CommonSearch {
    private String ptId;
    private EUserCourseStatus status;
    private String courseId;
}
