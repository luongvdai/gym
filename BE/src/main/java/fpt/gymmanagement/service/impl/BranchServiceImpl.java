package fpt.gymmanagement.service.impl;


import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.BranchSearch;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.entity.QBranch;
import fpt.gymmanagement.repository.BranchRepository;
import fpt.gymmanagement.service.BranchService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service
public class BranchServiceImpl extends CommonJPAServiceImpl<Branch, String, BranchRepository, QBranch>
        implements BranchService {

    public BranchServiceImpl(EntityManager em, BranchRepository repo) {
        super(em, QBranch.branch, repo);
    }


    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy chi nhánh!!!";
    }

    @Override
    public Page<Branch> getPage(BranchSearch search) {
        this.addFilter(search);
        return (Page<Branch>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    private void addFilter(BranchSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);
        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.name.containsIgnoreCase(keyword)
                    .or(q.address.containsIgnoreCase(keyword)));
        }
        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}

