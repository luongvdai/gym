package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PTInCourseSearch extends CommonSearch {
    private String courseId;
    private String keyword;
    private Boolean isActive;
    private String branchId;
}
