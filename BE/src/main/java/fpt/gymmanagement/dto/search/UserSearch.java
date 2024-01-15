package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserSearch extends CommonSearch {
    private Boolean gender;
    private Boolean isActive;
    private Long roleId;
    private Boolean isVerify;
    private String branchId;
    private String currentAccount;
}
