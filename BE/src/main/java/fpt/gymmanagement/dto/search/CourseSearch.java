package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CourseSearch extends CommonSearch {
    @Min(value = 1)
    @Max(value = 4)
    private Integer rangePrice;
    @Min(value = 1)
    @Max(value = 13)
    private Integer rangeProcess;
    private String typeId;
    private String branchId;
    private Boolean newest;
    private Boolean isActive;
}
