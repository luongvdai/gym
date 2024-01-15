package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoursesOfPTSearch extends CommonSearch {
    private String ptId;
    private String keyword;
}
