package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.GuestConsultantSearch;
import fpt.gymmanagement.entity.GuestConsultant;
import fpt.gymmanagement.entity.QGuestConsultant;
import fpt.gymmanagement.repository.GuestConsultantRepository;
import fpt.gymmanagement.service.GuestConsultantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service
public class GuestConsultantServiceImpl extends CommonJPAServiceImpl<GuestConsultant, String, GuestConsultantRepository, QGuestConsultant>
implements GuestConsultantService {
    public GuestConsultantServiceImpl(EntityManager em, GuestConsultantRepository repo) {
        super(em, QGuestConsultant.guestConsultant, repo);
    }

    @Override
    protected String notFoundMessage() {
        return null;
    }

    @Override
    public Page<GuestConsultant> getPage(GuestConsultantSearch search) {
        this.addFilterCourse(search);
        long count = this.query.stream().count();
        this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize());
        var data = this.query.fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    private void addFilterCourse(GuestConsultantSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.name.containsIgnoreCase(keyword));
        }

        if (search.getStatus() != null) {
            this.query.where(q.status.eq(search.getStatus()));
        }

        if (search.getBranch() != null) {
            this.query.where(q.branchId.eq(search.getBranch()));
        }
        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}
