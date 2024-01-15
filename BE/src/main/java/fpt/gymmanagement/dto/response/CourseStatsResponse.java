package fpt.gymmanagement.dto.response;

import fpt.gymmanagement.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStatsResponse {
    private String courseId;
    private String courseName;
    private String branchId;
    private String branchName;
    private Integer total;
}
