package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackSearch extends CommonSearch {
    private String ptId;
}
