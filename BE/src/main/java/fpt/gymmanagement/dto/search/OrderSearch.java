package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import fpt.gymmanagement.enums.EOrderStatus;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderSearch extends CommonSearch {
    private String userId;
    private String branchId;
    private Boolean isManage;
    private EOrderStatus status;
    private String buyer;
    private String assignId;
    private String approveId;
}
