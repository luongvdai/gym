package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BlogSearch extends CommonSearch {
    private String typeId;
    private String createdBy;
    private String branchId;
}
