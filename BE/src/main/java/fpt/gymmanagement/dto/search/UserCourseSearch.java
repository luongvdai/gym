package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import fpt.gymmanagement.enums.EUserCourseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCourseSearch extends CommonSearch {
    private String userId;
    private String ptCourseId;
    private EUserCourseStatus status;
}
