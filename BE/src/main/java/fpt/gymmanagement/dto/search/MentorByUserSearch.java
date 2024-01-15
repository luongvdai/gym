package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorByUserSearch extends CommonSearch {
    private String ptId;
    private String userId;
}
