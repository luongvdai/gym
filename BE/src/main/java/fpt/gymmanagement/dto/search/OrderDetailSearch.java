package fpt.gymmanagement.dto.search;

import fpt.gymmanagement.common.CommonSearch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailSearch extends CommonSearch {
    private String orderId;
}
