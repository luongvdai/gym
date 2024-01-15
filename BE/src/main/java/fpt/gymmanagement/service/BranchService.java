package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.BranchSearch;
import fpt.gymmanagement.entity.Branch;
import org.springframework.data.domain.Page;

public interface BranchService extends CommonService<Branch, String> {
    Page<Branch> getPage(BranchSearch search);
}
